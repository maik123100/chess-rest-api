package local.chess.chessRestApi.model;

import java.io.Serializable;
import java.util.Objects;

public class AnalysisId implements Serializable {
	private String uuid;
	private String playedMove;

	public AnalysisId() {
	}

	public AnalysisId(String uuid, String playedMove) {
		this.uuid = uuid;
		this.playedMove = playedMove;
	}

	// Getters and Setters:
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AnalysisId that = (AnalysisId) o;
		return Objects.equals(uuid, that.uuid) && Objects.equals(playedMove, that.playedMove);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, playedMove);
	}
}
