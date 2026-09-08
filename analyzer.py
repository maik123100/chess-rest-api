#!/usr/bin/env python3
"""
analyzer.py - Stockfish analysis worker for chess-rest-api

Finds requested games, runs Stockfish on each move, and writes results
directly to Postgres.

Usage:
    python3 analyzer.py              # Analyze all unanalyzed requested games, then exit
    python3 analyzer.py --game <id>  # Analyze a specific game by ID
"""

import os
import sys
import json
import chess
import chess.engine
import psycopg2
from dotenv import load_dotenv

STOCKFISH_PATH = None
STOCKFISH_DEPTH = None
STOCKFISH_THREADS = None


def load_config():
    global STOCKFISH_PATH, STOCKFISH_DEPTH, STOCKFISH_THREADS
    STOCKFISH_PATH = os.getenv("STOCKFISH_PATH", "/usr/games/stockfish")
    STOCKFISH_DEPTH = int(os.getenv("STOCKFISH_DEPTH", "11"))
    STOCKFISH_THREADS = int(os.getenv("STOCKFISH_THREADS", "4"))


def get_db_connection():
    return psycopg2.connect(
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        host=os.getenv("DB_HOST"),
    )


def score_to_float(score):
    if hasattr(score, "relative"):
        score = score.relative
    if score.is_mate():
        mate_in = score.mate()
        return 100.0 if mate_in > 0 else -100.0
    return score.score() / 100.0


def analyze_game(game_id):
    conn = get_db_connection()
    try:
        with conn.cursor() as cur:
            cur.execute(
                """SELECT id, fen, initial_setup, parsed_moves
                   FROM games WHERE id = %s AND (analyzed = false OR analyzed IS NULL)""",
                (game_id,),
            )
            row = cur.fetchone()
            if not row:
                print(f"  Game {game_id}: not found or already analyzed, skipping.")
                return

            db_id, final_fen, initial_setup, parsed_moves_json = row
            moves = json.loads(parsed_moves_json) if parsed_moves_json else []
            if not moves:
                print(f"  Game {game_id}: no parsed moves, skipping.")
                return

            start_fen = initial_setup or chess.STARTING_FEN
            print(f"  Game {game_id}: analyzing {len(moves)} moves...")

            engine = chess.engine.SimpleEngine.popen_uci(STOCKFISH_PATH)
            engine.configure({"Threads": STOCKFISH_THREADS})
            board = chess.Board(start_fen)

            cur.execute(
                """INSERT INTO analysis (game_id, engine, depth)
                   VALUES (%s, 'stockfish', %s)
                   RETURNING id""",
                (game_id, STOCKFISH_DEPTH),
            )
            analysis_id = cur.fetchone()[0]

            evaluations = []
            for i, move_san in enumerate(moves):
                move = board.parse_san(move_san)
                result = engine.analyse(board, chess.engine.Limit(depth=STOCKFISH_DEPTH))
                engine_move = result["pv"][0]
                engine_eval = score_to_float(result["score"].white())
                engine_move_san = board.san(engine_move)

                board.push(move)
                after_result = engine.analyse(board, chess.engine.Limit(depth=STOCKFISH_DEPTH))
                played_eval = score_to_float(after_result["score"].white())

                evaluations.append({
                    "move_number": i + 1,
                    "move_played": move_san,
                    "engine_move": engine_move_san,
                    "engine_move_eval": engine_eval,
                    "eval_diff": round(engine_eval - played_eval, 4),
                })

            engine.quit()

            for ev in evaluations:
                cur.execute(
                    """INSERT INTO move_evaluations
                       (analysis_id, move_number, move_played, engine_move,
                        engine_move_eval, eval_diff)
                       VALUES (%s, %s, %s, %s, %s, %s)""",
                    (analysis_id, ev["move_number"], ev["move_played"],
                     ev["engine_move"], ev["engine_move_eval"], ev["eval_diff"]),
                )

            cur.execute(
                "UPDATE games SET analyzed = true WHERE id = %s",
                (game_id,),
            )
            conn.commit()
            print(f"  Game {game_id}: done ({len(evaluations)} moves analyzed).")

    except Exception as e:
        conn.rollback()
        print(f"  Game {game_id}: ERROR - {e}")
        raise
    finally:
        conn.close()


def analyze_pending():
    print("Analyzing all pending games...")
    conn = get_db_connection()
    with conn.cursor() as cur:
        cur.execute(
            "SELECT id FROM games WHERE analysis_requested = true AND (analyzed = false OR analyzed IS NULL)"
        )
        ids = [row[0] for row in cur.fetchall()]
    conn.close()

    if not ids:
        print("No pending games.")
        return

    print(f"Found {len(ids)} game(s) to analyze.")
    for game_id in ids:
        analyze_game(game_id)


def main():
    load_dotenv()
    load_config()
    if "--game" in sys.argv:
        idx = sys.argv.index("--game")
        if idx + 1 < len(sys.argv):
            analyze_game(int(sys.argv[idx + 1]))
        else:
            print("Usage: python3 analyzer.py --game <id>")
            sys.exit(1)
    else:
        analyze_pending()


if __name__ == "__main__":
    main()
