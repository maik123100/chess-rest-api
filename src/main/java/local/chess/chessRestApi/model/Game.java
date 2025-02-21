package local.chess.chessRestApi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

	@Id
	private String uuid;

	private String whiteUsername;
	private Integer whiteRating;
	private String blackUsername;
	private Integer blackRating;
	private String timeControl;
	private String pgn;
	private String win;

	// Getters and Setters:

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWhiteUsername() {
		return whiteUsername;
	}

	public void setWhiteUsername(String whiteUsername) {
		this.whiteUsername = whiteUsername;
	}

	public Integer getWhiteRating() {
		return whiteRating;
	}

	public void setWhiteRating(Integer whiteRating) {
		this.whiteRating = whiteRating;
	}

	public String getBlackUsername() {
		return blackUsername;
	}

	public void setBlackUsername(String blackUsername) {
		this.blackUsername = blackUsername;
	}

	public Integer getBlackRating() {
		return blackRating;
	}

	public void setBlackRating(Integer blackRating) {
		this.blackRating = blackRating;
	}

	public String getTimeControl() {
		return timeControl;
	}

	public void setTimeControl(String timeControl) {
		this.timeControl = timeControl;
	}

	public String getPgn() {
		return pgn;
	}

	public void setPgn(String pgn) {
		this.pgn = pgn;
	}

	public String getWin() {
		return win;
	}

	public void setWin(String win) {
		this.win = win;
	}
}
