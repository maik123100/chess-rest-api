#!/usr/bin/env python3
"""
pullGames.py - Chess.com game puller for chess-rest-api

Fetches games from chess.com API and inserts them into the PostgreSQL database
managed by the Spring Boot REST API.

Usage:
    python3 pullGames.py              # Single pull (up to last stored game)
    python3 pullGames.py --listen     # Long-running NOTIFY listener
"""

import os
import sys
import json
import io
import requests
import chess.pgn
from datetime import datetime, timedelta
from dotenv import load_dotenv
import psycopg2

CHESS_API_BASE = "https://api.chess.com/pub/player"
HEADERS = {"User-Agent": "chess-rest-api/1.0 (github.com/maik/chess-rest-api)"}

RESULT_WIN = {"win"}
RESULT_LOSS = {"checkmated", "timeout", "resigned", "abandoned", "lose"}
RESULT_DRAW = {"draw", "stalemate", "agreed", "repetition", "insufficient"}


def get_db_connection():
    return psycopg2.connect(
        dbname=os.getenv("DB_NAME"),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        host=os.getenv("DB_HOST"),
    )


def fetch_month_games(username, year, month):
    url = f"{CHESS_API_BASE}/{username}/games/{year:04d}/{month:02d}"
    try:
        resp = requests.get(url, headers=HEADERS, timeout=30)
        resp.raise_for_status()
        return resp.json().get("games", [])
    except requests.RequestException as e:
        print(f"Error fetching games from {url}: {e}")
        return []


def fetch_last_month_games(username):
    now = datetime.now()
    current = fetch_month_games(username, now.year, now.month)
    prev = now - timedelta(days=30)
    previous = fetch_month_games(username, prev.year, prev.month)
    return previous + current


def normalize_result(chess_com_result):
    r = chess_com_result.lower()
    if r in RESULT_WIN:
        return "win"
    if r in RESULT_LOSS:
        return "loss"
    if r in RESULT_DRAW:
        return "draw"
    return None


def parse_moves_from_pgn(pgn_text):
    game = chess.pgn.read_game(io.StringIO(pgn_text))
    if game is None:
        return []
    moves = []
    board = game.board()
    for move in game.mainline_moves():
        moves.append(board.san(move))
        board.push(move)
    return moves


def parse_eco(eco_url):
    if not eco_url:
        return None, None, None
    parts = eco_url.rstrip("/").split("/")
    slug = parts[-1] if parts else None
    if slug:
        opening_name = slug.replace("-", " ")
        return None, opening_name, eco_url
    return None, None, eco_url


def get_user_result(game, username):
    white = game.get("white", {})
    black = game.get("black", {})
    if white.get("username", "").lower() == username.lower():
        return normalize_result(white.get("result", ""))
    if black.get("username", "").lower() == username.lower():
        return normalize_result(black.get("result", ""))
    return None


def upsert_user(cursor, username, rating, url):
    cursor.execute(
        """INSERT INTO users (username, rating, url)
           VALUES (%s, %s, %s)
           ON CONFLICT (username) DO UPDATE
           SET rating = EXCLUDED.rating, url = EXCLUDED.url
           RETURNING id""",
        (username, rating, url),
    )
    return cursor.fetchone()[0]


def insert_game(cursor, game, username):
    if not game.get("rated", False):
        return False

    external_id = game.get("uuid")
    if not external_id:
        return False

    pgn_text = game.get("pgn", "")
    parsed_moves = parse_moves_from_pgn(pgn_text)

    time_class = game.get("time_class", "").upper()
    if not time_class:
        return False

    eco_code, opening_name, eco_url = parse_eco(game.get("eco"))

    white_data = game.get("white", {})
    black_data = game.get("black", {})

    white_id = upsert_user(
        cursor, white_data["username"], white_data.get("rating"),
        white_data.get("@id"),
    )
    black_id = upsert_user(
        cursor, black_data["username"], black_data.get("rating"),
        black_data.get("@id"),
    )

    result = get_user_result(game, username)

    cursor.execute(
        """INSERT INTO games
               (external_id, pgn, parsed_moves, time_class, rated,
                initial_setup, fen, rules,
                eco_code, opening_name, eco_url,
                white_id, white_rating, white_result,
                black_id, black_rating, black_result,
                result)
           VALUES (%s, %s, %s, %s, %s,
                   %s, %s, %s,
                   %s, %s, %s,
                   %s, %s, %s,
                   %s, %s, %s,
                   %s)
           ON CONFLICT (external_id) DO NOTHING""",
        (
            external_id,
            pgn_text,
            json.dumps(parsed_moves),
            time_class,
            True,
            game.get("initial_setup"),
            game.get("fen"),
            game.get("rules"),
            eco_code,
            opening_name,
            eco_url,
            white_id,
            white_data.get("rating"),
            white_data.get("result"),
            black_id,
            black_data.get("rating"),
            black_data.get("result"),
            result,
        ),
    )
    return True


def get_stored_external_ids(conn):
    with conn.cursor() as cur:
        cur.execute("SELECT external_id FROM games")
        return {row[0] for row in cur.fetchall()}


def pull_up_to_last_game(username):
    print(f"Pulling games for {username}...")
    games = fetch_last_month_games(username)
    if not games:
        print("No games fetched from chess.com.")
        return

    games.sort(key=lambda g: g.get("end_time", 0), reverse=True)

    conn = get_db_connection()
    stored_ids = get_stored_external_ids(conn)

    inserted = 0
    skipped_unrated = 0
    skipped_existing = 0

    try:
        with conn.cursor() as cur:
            for game in games:
                external_id = game.get("uuid")
                if external_id in stored_ids:
                    print(f"Reached already-stored game ({external_id}). Stopping.")
                    skipped_existing += 1
                    break

                if not game.get("rated", False):
                    skipped_unrated += 1
                    continue

                try:
                    if insert_game(cur, game, username):
                        inserted += 1
                        print(f"  Inserted: {external_id}")
                except Exception as e:
                    print(f"  Error inserting {external_id}: {e}")
                    conn.rollback()
                    continue

            conn.commit()
    finally:
        conn.close()

    print(
        f"Done. Inserted: {inserted}, "
        f"Skipped (unrated): {skipped_unrated}, "
        f"Stopped at existing: {skipped_existing}"
    )


def listen_for_refreshes(username):
    print(f"Listening for game_refresh notifications (user: {username})...")
    conn = get_db_connection()
    conn.autocommit = True
    cursor = conn.cursor()
    cursor.execute("LISTEN game_refresh;")
    try:
        while True:
            conn.poll()
            while conn.notifies:
                notify = conn.notifies.pop(0)
                print(f"Received refresh notification: {notify.payload}")
                pull_up_to_last_game(username)
    except KeyboardInterrupt:
        print("\nShutting down listener.")
    finally:
        cursor.close()
        conn.close()


def main():
    load_dotenv()
    username = os.getenv("CHESS_USERNAME", "cs4d61696b")
    if "--listen" in sys.argv:
        listen_for_refreshes(username)
    else:
        pull_up_to_last_game(username)


if __name__ == "__main__":
    main()
