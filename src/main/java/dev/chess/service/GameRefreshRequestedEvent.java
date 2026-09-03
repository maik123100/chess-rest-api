package dev.chess.service;

import java.time.Instant;

public record GameRefreshRequestedEvent(Instant triggeredAt) {
}
