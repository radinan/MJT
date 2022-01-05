package bg.sofia.uni.fmi.mjt.logger;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {

    private final int MIN_INDEX_OF_LOG_FILE = 0;

    private LoggerOptions options;
    private int currentIndexOfLogFile = MIN_INDEX_OF_LOG_FILE;
    //name = "logs-" + currentIndexOfLogFile + ".txt"
    public DefaultLogger(LoggerOptions options) {
        this.options = options;
    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null || timestamp == null ||
                message == null || message.isEmpty()) {
            throw new IllegalArgumentException();
        }

/*        try {

        } catch (Exception e) {
            if (options.shouldThrowErrors() == true) {
                //throw ???
            }
        }*/
    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return null;
//        return Path.of("");
    }
}
