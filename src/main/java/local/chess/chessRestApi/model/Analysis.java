package local.chess.chessRestApi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "analysis")
public class Analysis {

	@Id
	private String uuid;

	@Column(columnDefinition = "TEXT")
	private String analysis; // JSON array of {played_move, best_line, score}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
}
