package local.chess.chessRestApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import local.chess.chessRestApi.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByOrderByDateDesc();
}