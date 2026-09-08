package dev.chess.controller;

import dev.chess.service.AnalysisRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/games")
public class GameController {

    private final AnalysisRequestService analysisRequestService;

    public GameController(AnalysisRequestService analysisRequestService) {
        this.analysisRequestService = analysisRequestService;
    }

    @PostMapping("/{id}/analysis-request")
    public ResponseEntity<?> requestAnalysis(@PathVariable Long id) {
        try {
            analysisRequestService.requestAnalysis(id);
            return ResponseEntity.ok(Map.of("status", "analysis-requested", "gameId", String.valueOf(id)));
        } catch (AnalysisRequestService.GameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}