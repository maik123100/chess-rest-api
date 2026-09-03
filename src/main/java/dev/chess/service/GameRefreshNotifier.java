package dev.chess.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class GameRefreshNotifier {

    private final PostgresNotifier notifier;

    public GameRefreshNotifier(PostgresNotifier notifier) {
        this.notifier = notifier;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRefreshRequested(GameRefreshRequestedEvent event) {
        notifier.notify("game_refresh", "{}");
    }
}
