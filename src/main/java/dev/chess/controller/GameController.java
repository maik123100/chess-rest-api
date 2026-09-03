package dev.chess.controller;

import dev.chess.service.AnalysisRequestService;
import dev.chess.service.GameRefreshService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameRefreshService gameRefreshService;
    private final AnalysisRequestService analysisRequestService;

    public GameController(GameRefreshService gameRefreshService,
                          AnalysisRequestService analysisRequestService) {
        this.gameRefreshService = gameRefreshService;
        this.analysisRequestService = analysisRequestService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshGames() {
        gameRefreshService.requestRefresh();
        return ResponseEntity.accepted().body(Map.of("status", "refresh-queued"));
    }

    @PostMapping("/{id}/analysis-request")
    public ResponseEntity<?> requestAnalysis(@PathVariable Long id) {
        try {
            analysisRequestService.requestAnalysis(id);
            return ResponseEntity.accepted().body(Map.of("status", "analysis-requested", "gameId", String.valueOf(id)));
        } catch (AnalysisRequestService.GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
