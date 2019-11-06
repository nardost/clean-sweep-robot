package com.team4.commons;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LogManager {

    public static void print(String msg, long zeroTime) {
        StringBuilder sb = new StringBuilder(formatElapsedTime(System.currentTimeMillis(), zeroTime));
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public static void logForUnity(Location location, boolean floorIsCleanBefore, boolean floorIsCleanAfter, long zeroTime) {
        StringBuilder simple = new StringBuilder();
        simple.append(Utilities.padSpacesToFront(location.toString(), 8));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorIsCleanBefore) ? "CLEAN     --> " : "NOT CLEAN --> ", 9));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorIsCleanAfter) ? "CLEAN" : "NOT CLEAN", 9));
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile"));
        textFileLogger.log(simple.toString());
    }
    public static void logForUnity(Location location) {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(location.toString());
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile"));
        textFileLogger.log(sb.toString());
    }

    private static String formatElapsedTime(long milliTime, long zeroTime) {
        long elapsedTime = milliTime - zeroTime;
        long elapsedSeconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);
        long s = elapsedSeconds % 60;
        long m = ((elapsedSeconds - s) % 3600) / 60;
        long h = (elapsedSeconds - (elapsedSeconds - s) % 3600) / 60;
        return String.format("[%02d:%02d:%02d]  ", h, m, s);
    }
}
