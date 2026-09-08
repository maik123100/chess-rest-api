package dev.chess.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class RunnerDispatchService {

    private final String pythonCommand;
    private final Path workingDirectory;
    private final Path pullGamesScript;
    private final Path analyzerScript;
    private final Path logFile;
    private final Path errorFile;

    public RunnerDispatchService(
            @Value("${runner.python-command:python3}") String pythonCommand,
            @Value("${runner.working-directory:.}") String workingDirectory,
            @Value("${runner.pull-games-script:pullGames.py}") String pullGamesScript,
            @Value("${runner.analyzer-script:analyzer.py}") String analyzerScript,
            @Value("${runner.log-file:runner.log}") String logFile,
            @Value("${runner.error-file:runner.err}") String errorFile) {
        this.pythonCommand = pythonCommand;
        this.workingDirectory = Path.of(workingDirectory).toAbsolutePath().normalize();
        this.pullGamesScript = resolve(workingDirectory, pullGamesScript);
        this.analyzerScript = resolve(workingDirectory, analyzerScript);
        this.logFile = resolve(workingDirectory, logFile);
        this.errorFile = resolve(workingDirectory, errorFile);
    }

    public void dispatchGamePull() {
        dispatch(List.of(pythonCommand, pullGamesScript.toString()));
    }

    public void dispatchAnalysis() {
        dispatch(List.of(pythonCommand, analyzerScript.toString()));
    }

    private void dispatch(List<String> command) {
        try {
            Files.createDirectories(workingDirectory);
            createParentDirectory(logFile);
            createParentDirectory(errorFile);

            ProcessBuilder processBuilder = new ProcessBuilder(command)
                    .directory(workingDirectory.toFile())
                    .redirectOutput(ProcessBuilder.Redirect.appendTo(logFile.toFile()))
                    .redirectError(ProcessBuilder.Redirect.appendTo(errorFile.toFile()));
            processBuilder.start();
        } catch (IOException e) {
            appendDispatchError(command, e);
        }
    }

    private void appendDispatchError(List<String> command, IOException exception) {
        try {
            createParentDirectory(errorFile);
            Files.writeString(errorFile,
                    "Unable to dispatch " + String.join(" ", command) + ": "
                            + exception.getMessage() + System.lineSeparator(),
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // There is no other process available to report a dispatch failure.
        }
    }

    private void createParentDirectory(Path file) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private Path resolve(String baseDirectory, String configuredPath) {
        Path path = Path.of(configuredPath);
        return path.isAbsolute()
                ? path.normalize()
                : Path.of(baseDirectory).toAbsolutePath().normalize().resolve(path).normalize();
    }
}