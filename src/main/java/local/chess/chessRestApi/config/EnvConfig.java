package local.chess.chessRestApi.config;

import org.springframework.context.annotation.Configuration;
import io.github.cdimascio.dotenv.Dotenv;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {
    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }
}