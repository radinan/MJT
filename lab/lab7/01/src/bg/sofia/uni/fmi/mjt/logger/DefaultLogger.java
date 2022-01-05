package bg.sofia.uni.fmi.mjt.logger;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {

    private final int MIN_INDEX_OF_LOG_FILE = 0;
    private final String FILE_KEY_NAME = "logs-";
    private final String FILE_EXTENSION = ".txt";

    private LoggerOptions options;
    private int currentIndex = MIN_INDEX_OF_LOG_FILE;

    public DefaultLogger(LoggerOptions options) {
        this.options = options;
    }

    /**
     * Logs message to the current log file. If the currently configured minimum log
     * level is higher than the provided log level, the message should be ignored.
     *
     * @param level     the log message severity
     * @param timestamp the time of logging
     * @param message   log message
     * @throws IllegalArgumentException if {@code level} is null, {@code timestamp} is null
     *                                  or {@code message} is null or empty
     * @throws LogException             if LoggerOptions.shouldThrowErrors() is true and if the operation cannot be completed
     *                                  In case LoggerOptions.shouldThrowErrors() is false, the method should suppress any problems
     */
    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null ||
                message == null || message.isEmpty()) {
            throw new IllegalArgumentException();
        }

        try /*()*/{
            if (level.getLevel() >= options.getMinLogLevel().getLevel()) {
                //log
                //check storage
                //next version
            }
        } catch (Exception e) {
            if (options.shouldThrowErrors()) {
                throw new LogException();
            }
        }
    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return Path.of(String.format("%s%s%s%s",
                options.getDirectory(),
                FILE_KEY_NAME,
                currentIndex,
                FILE_EXTENSION));
    }
}
