package dev.chess.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class RunnerDispatchService {

    private final String pythonCommand;
    private final Path workingDirectory;

    public RunnerDispatchService(
            @Value("${runner.python-command:venv/bin/python3}") String pythonCommand,
            @Value("${runner.working-directory:.}") String workingDirectory) {
        this.pythonCommand = pythonCommand;
        this.workingDirectory = Path.of(workingDirectory).toAbsolutePath().normalize();
    }

    public void dispatchGamePull() {
        dispatch("pullGames.py", "pullgames.log", "pullgames.err", List.of());
    }

    public void dispatchAnalysis() {
        dispatch("analyzer.py", "analyzer.log", "analyzer.err", List.of());
    }

    private void dispatch(String script, String logFile, String errorFile, List<String> args) {
        List<String> command = new ArrayList<>();
        command.add(pythonCommand);
        command.add(workingDirectory.resolve(script).toString());
        command.addAll(args);

        Path logPath = workingDirectory.resolve(logFile);
        Path errorPath = workingDirectory.resolve(errorFile);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command)
                    .directory(workingDirectory.toFile())
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(logPath.toFile()))
                    .redirectError(ProcessBuilder.Redirect.appendTo(errorPath.toFile()));
            processBuilder.start();
        } catch (IOException e) {
            try {
                java.nio.file.Files.writeString(errorPath,
                        "Failed to start " + String.join(" ", command) + ": " + e.getMessage()
                                + System.lineSeparator(),
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            } catch (IOException ignored) {
                // No process available to report the failure.
            }
        }
    }
}
