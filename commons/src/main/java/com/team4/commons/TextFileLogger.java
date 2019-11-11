package com.team4.commons;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TextFileLogger {

    private String logFile;

    public TextFileLogger(String logFile) {
        try {
            String unityLogHome = ConfigManager.getConfiguration("unityLogHome");
            File logsDirectory = new File(System.getenv(unityLogHome));
            setLogFile(logsDirectory.getAbsolutePath() + File.separator + logFile);
            File file = new File(getLogFile());
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
            Files.write(Paths.get(getLogFile()), (logMessage + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch(IOException ioe) {
            throw new RobotException("ERROR: while writing to log file " + getLogFile());
        }
    }

    String getLogFile() {
        return logFile;
    }

    void setLogFile(String logFile) {
        if(logFile == null) {
            throw new RobotException("Null file name not allowed.");
        }
        this.logFile = logFile;
    }
}
