package dev.chess.valueobject;

import java.util.Objects;

public class Fen {

    private final String value;

    public Fen(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FEN must not be null or blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fen fen)) return false;
        return Objects.equals(value, fen.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
