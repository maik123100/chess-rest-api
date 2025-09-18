package local.chess.chessRestApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import local.chess.chessRestApi.model.Analysis;

public interface AnalysisRepository extends JpaRepository<Analysis, String> {
	Analysis findByGameUuid(String uuid);
}
