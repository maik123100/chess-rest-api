package dev.chess.controller;

import dev.chess.service.RunnerDispatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {

    private final RunnerDispatchService runnerDispatchService;

    public DispatchController(RunnerDispatchService runnerDispatchService) {
        this.runnerDispatchService = runnerDispatchService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh() {
        runnerDispatchService.dispatchGamePull();
        return ResponseEntity.accepted().body(Map.of("status", "refresh-dispatched"));
    }

    @PostMapping("/analysis")
    public ResponseEntity<Map<String, String>> analysis() {
        runnerDispatchService.dispatchAnalysis();
        return ResponseEntity.accepted().body(Map.of("status", "analysis-dispatched"));
    }
}