package dev.chess.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class GameRefreshNotifier {

    private final RunnerDispatchService runnerDispatchService;

    public GameRefreshNotifier(RunnerDispatchService runnerDispatchService) {
        this.runnerDispatchService = runnerDispatchService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRefreshRequested(GameRefreshRequestedEvent event) {
        runnerDispatchService.dispatchGamePull();
    }
}
