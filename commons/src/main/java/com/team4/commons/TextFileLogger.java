package com.team4.commons;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TextFileLogger {

    private String logFile;

    public TextFileLogger(String logFile) {
        this.logFile = logFile;
        try {
            File logsDirectory = new File(System.getenv("UNITY_LOGS_HOME"));
            File file = new File(logsDirectory.getAbsolutePath() + File.separator + logFile);
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch(IOException ioe) {
            throw new RobotException("I/O error while creating log file.");
        } catch(NullPointerException npe) {
            throw new RobotException("ERROR: the log file pathname is null.");
        }
    }

    public void log(String logMessage) {
        try {
            Files.write(Paths.get(System.getenv("UNITY_LOGS_HOME") + File.separator + logFile), (logMessage + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch(IOException ioe) {
            System.out.println(logFile);
            throw new RobotException("ERROR: while writing to log file.");
        }
    }
}
