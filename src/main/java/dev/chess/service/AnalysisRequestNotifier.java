package dev.chess.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AnalysisRequestNotifier {

    private final PostgresNotifier notifier;

    public AnalysisRequestNotifier(PostgresNotifier notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAnalysisRequested(AnalysisRequestedEvent event) {
        notifier.notify("analysis_requested", "{\"gameId\":" + event.gameId() + "}");
    }
}