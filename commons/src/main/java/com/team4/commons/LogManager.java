package com.team4.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    public static void print(String msg, long zeroTime) {
        StringBuilder sb = new StringBuilder(Utilities.formatElapsedTime(System.currentTimeMillis(), zeroTime));
        sb.append(msg);

        System.out.println(sb.toString());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String logFileName = formatter.format(new Date()) + ".log";
        TextFileLogger textFileLogger = new TextFileLogger(logFileName);
        textFileLogger.log(sb.toString());
    }

    /**
     * Save performance figures for each run.
     * @param zeroTime
     * @param currentMillis
     * @param initLocation
     * @param lastLocation
     * @param percentDone
     */
    //START_DATETIME    FINISH_DATETIME INITIAL_LOCATION    FINAL_LOCATION  PERCENTAGE_DONE TIME_TAKEN
    public static void logCurrentRun(long zeroTime, long currentMillis, String initLocation, String lastLocation, double percentDone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date startTime = new Date(zeroTime);
        Date finishTime = new Date(currentMillis);
        long duration = currentMillis - zeroTime;
        StringBuilder sb = new StringBuilder();
        sb.append(formatter.format(startTime));
        sb.append("\t");
        sb.append(formatter.format(finishTime));
        sb.append("\t");
        sb.append(initLocation);
        sb.append("\t");
        sb.append(lastLocation);
        sb.append("\t");
        sb.append(percentDone);
        sb.append("\t");
        sb.append(duration);

        String currentRunLogFile = "runs.log";
        TextFileLogger textFileLogger = new TextFileLogger(currentRunLogFile);
        textFileLogger.log(sb.toString());
    }

    public static void logForUnity(Location location, boolean floorIsCleanBefore, boolean floorIsCleanAfter, String batteryAfter, String dirtAfter, int numberOfRuns) {
        StringBuilder simple = new StringBuilder();
        String loc = location.toString().replace(", ", ",");
        simple.append(Utilities.padSpacesToFront("pos" + loc.replace("(", "[").replace(")", "]"), 8));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorIsCleanBefore) ? "act[ALREADY_CLEAN]" : "", 0));
        
        simple.append(Utilities.padSpacesToFront((floorIsCleanAfter && !floorIsCleanBefore) ? "act[CLEAN]" : "",0));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront( "b[" + batteryAfter + "]"  ,0));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront( "d[" + dirtAfter + "]"  ,0));
        
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile") + "-" + numberOfRuns + ".txt");
        textFileLogger.log(simple.toString());
    }
    public static void logForUnity(Location location, String state, String batteryAfter, String dirtAfter,  int numberOfRuns) {
        StringBuilder simple = new StringBuilder();
        String loc = location.toString().replace(", ", ",");
        simple.append(Utilities.padSpacesToFront("pos" + loc.replace("(", "[").replace(")", "]"), 8));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront( "act[" + state + "]"  ,0));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront( "b[" + batteryAfter + "]"  ,0));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront( "d[" + dirtAfter + "]"  ,0));
        TextFileLogger textFileLogger = new TextFileLogger(ConfigManager.getConfiguration("unityLogFile") + "-" + numberOfRuns + ".txt");
        textFileLogger.log(simple.toString());
    }
}
