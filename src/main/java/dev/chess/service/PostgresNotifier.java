package dev.chess.service;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class PostgresNotifier {

    private final DataSource dataSource;

    public PostgresNotifier(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void notify(String channel, String payload) {
        try (var conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("NOTIFY \"" + channel + "\", '" + payload + "'");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to send NOTIFY on channel " + channel, e);
        }
    }
}
