package dev.chess.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnalysisRequestNotifier {

    private final RunnerDispatchService runnerDispatchService;

    public AnalysisRequestNotifier(RunnerDispatchService runnerDispatchService) {
        this.runnerDispatchService = runnerDispatchService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAnalysisRequested(AnalysisRequestedEvent event) {
        runnerDispatchService.dispatchAnalysis();
    }
}