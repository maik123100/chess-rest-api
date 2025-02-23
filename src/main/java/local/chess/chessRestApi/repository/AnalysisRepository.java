package local.chess.chessRestApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import local.chess.chessRestApi.model.Analysis;
import local.chess.chessRestApi.model.AnalysisId;

public interface AnalysisRepository extends JpaRepository<Analysis, AnalysisId> {

	@Query("SELECT a FROM Analysis a WHERE a.uuid= :uuid")
	List<Analysis> findByGameUuid(String uuid);
}
