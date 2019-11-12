package com.team4.commons;

public class LogManager {

    public static void print(String msg, long zeroTime) {
        StringBuilder sb = new StringBuilder(Utilities.formatElapsedTime(System.currentTimeMillis(), zeroTime));
        sb.append(msg);
        System.out.println(sb.toString());
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
