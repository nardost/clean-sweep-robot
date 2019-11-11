package com.team4.commons;

public class LogManager {

    public static void print(String msg, long zeroTime) {
        StringBuilder sb = new StringBuilder(Utilities.formatElapsedTime(System.currentTimeMillis(), zeroTime));
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public static void logForUnity(Location location, boolean floorIsCleanBefore, boolean floorIsCleanAfter, int numberOfRuns) {
        StringBuilder simple = new StringBuilder();
        simple.append(Utilities.padSpacesToFront(location.toString(), 8));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorIsCleanBefore) ? "CLEAN     --> " : "NOT CLEAN --> ", 9));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorIsCleanAfter) ? "CLEAN" : "NOT CLEAN", 9));
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile") + "-" + numberOfRuns + ".txt");
        textFileLogger.log(simple.toString());
    }
    public static void logForUnity(Location location, int numberOfRuns) {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(location.toString());
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile") + "-" + numberOfRuns + ".txt");
        textFileLogger.log(sb.toString());
    }
}
