package dev.chess.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class GameRefreshService {

    private final ApplicationEventPublisher eventPublisher;

    public GameRefreshService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void requestRefresh() {
        eventPublisher.publishEvent(new GameRefreshRequestedEvent(Instant.now()));
    }
}
