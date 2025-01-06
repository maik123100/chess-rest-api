package local.chess.chess_rest_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import local.chess.chess_rest_api.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByOrderByDateDesc();
}