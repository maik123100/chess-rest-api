package local.chess.chessRestApi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "analysis")
public class Analysis {

	@Id
	private String uuid;
	private String playedMove;
	private String bestLine;
	private String score;

	@ManyToOne
	@JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
	private Game game;

	// Getters and setters
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlayedMove() {
		return playedMove;
	}

	public void setPlayedMove(String playedMove) {
		this.playedMove = playedMove;
	}

	public String getBestLine() {
		return bestLine;
	}

	public void setBestLine(String bestLine) {
		this.bestLine = bestLine;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
