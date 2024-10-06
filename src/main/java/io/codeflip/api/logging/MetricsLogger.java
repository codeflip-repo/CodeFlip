package io.codeflip.api.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MetricsLogger {
    private static final Logger logger = LoggerFactory.getLogger(MetricsLogger.class);
    private static final String LOG_DIR = "build/logs";
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH");
    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private FileWriter writer;
    private String currentFileName;

    public MetricsLogger() {
        createLogDirectory();
    }

    private void createLogDirectory() {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                logger.info("Created log directory: {}", LOG_DIR);
            } else {
                logger.error("Failed to create log directory: {}", LOG_DIR);
            }
        }
    }

    public void logMetric(String metricName, double value) {
        try {
            createOrRotateFile();
            LocalDateTime now = LocalDateTime.now();
            String logEntry = String.format("%s - %s: %.2f%n",
                    now.format(LOG_DATE_FORMAT), metricName, value);
            writer.write(logEntry);
            writer.flush();
        } catch (IOException e) {
            logger.error("Error logging metric", e);
        }
    }

    private void createOrRotateFile() throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String fileName = String.format("%s/metrics_%s.log", LOG_DIR, now.format(FILE_DATE_FORMAT));
        if (!fileName.equals(currentFileName)) {
            if (writer != null) {
                writer.close();
            }
            writer = new FileWriter(fileName, true);
            currentFileName = fileName;
            logger.info("Created new metrics log file: {}", fileName);
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.error("Error closing metrics log file", e);
            }
        }
    }
}