package com.team4.commons;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utilities {
    /**
     * Extracts the first entry of a tuple of form (x, y)
     * The parentheses and the comma are important. Otherwise, an
     * Exception should be thrown.
     *
     * @param tuple
     * @return int
     */
    public static int xFromTupleString(String tuple) {
        if(!isValidTuple(tuple)) {
            throw new RobotException(tuple + " is not a valid pair of coordinates.");
        }
        String parenthesisLeftRemoved = tuple.split("\\(")[1];
        String parenthesesRemoved = parenthesisLeftRemoved.split("\\)")[0];
        int x = Integer.parseInt(parenthesesRemoved.split(",")[0].replaceAll(" ", ""));
        return x;
    }
    public static int yFromTupleString(String tuple) {
        if(!isValidTuple(tuple)) {
            throw new RobotException(tuple + " is not a valid pair of coordinates.");
        }
        String parenthesisLeftRemoved = tuple.split("\\(")[1];
        String parenthesesRemoved = parenthesisLeftRemoved.split("\\)")[0];
        int y = Integer.parseInt(parenthesesRemoved.split(",")[1].replaceAll(" ", ""));
        return y;
    }

    /**
     * Verify tuple has correct tuple pattern (x, y)
     * @param tuple
     * @return boolean
     */
    public static boolean isValidTuple(String tuple) {
        return true;
    }

    /**
     * Converts a pair of comma separated non-negative integers
     * into a string.
     * @param x
     * @param y
     * @return "(x,y)"
     */
    public static String tupleToString(int x, int y) {
        if(x < 0 || y < 0) {
            throw new RobotException("Negative coordinates not allowed in tuple to string conversion.");
        }
        StringBuilder sb = new StringBuilder("(");
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(")");
        return sb.toString();
    }

    public static <T> String arrayToString(T [] elements) {
        StringBuilder sb = new StringBuilder("{ ");
        for(T element : elements) {
            sb.append(element.toString());
            sb.append(", ");
        }
        if(sb.length() > 2) {
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append(" }");
        return sb.toString();
    }

    public static String padSpacesToFront(String str, int fixedSize) {
        if(str.length() >= fixedSize) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= fixedSize - str.length(); i++) {
            sb.append(" ");
        }
        sb.append(str);
        return sb.toString();
    }

    public static int max(int a, int b) {
        return (a <= b) ? b : a;
    }

    public static int min(int a, int b) {
        return (a > b) ? b : a;
    }

    public static void printFormattedHeader(WorkingMode mode) {
        if(mode == WorkingMode.DEPLOYED) {
            System.out.println("--------------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
            System.out.println("         CLOCK  DIRECTION  LOCATION     BEFORE      AFTER  FLOOR TYPE  BATTERY BEFORE  BATTERY AFTER  DIRT LEVEL               OPEN DIRECTIONS\tCHARGING STATIONS NEARBY");
            System.out.println("--------------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
        }
    }

    public static void doTimeDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    public static void doLoopedTimeDelay(String msg, long millis, long zeroTime) {
        int numberOfLoops = (int) (millis / 1000L);
        try {
            for(int i = 1; i <= numberOfLoops; i++){
                LogManager.print(msg, zeroTime);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public static void printLogo() {
        System.out.println();
        System.out.println("                                             +=======================================================+");
        System.out.println("                                             |                     CLEAN SWEEP ROBOT                 |");
        System.out.println("                                             +=======================================================+");
        System.out.println();
    }
    public static void printConfiguration() {
        String initLocation = ConfigManager.getConfiguration("initLocation");
        String maxBatteryLevel = ConfigManager.getConfiguration("maxBatteryLevel");
        String dirtCapacity = ConfigManager.getConfiguration("dirtCapacity");
        String chargingStationDetectionRadius = ConfigManager.getConfiguration("chargingStationDetectionRadius");
        String navigator = ConfigManager.getConfiguration("navigator");
        String scheduledWait = ConfigManager.getConfiguration("scheduledWait");
        String timeInTile = ConfigManager.getConfiguration("timeInTile");
        String timeToCharge = ConfigManager.getConfiguration("timeToCharge");
        String floorBuilderType = ConfigManager.getConfiguration("floorBuilderType");
        String floorPlan = ConfigManager.getConfiguration("floorPlan");
        String dirtGeneratorType = ConfigManager.getConfiguration("dirtGeneratorType");
        String logsHome = ConfigManager.getConfiguration("logsHome");
        String summaryLogFile = ConfigManager.getConfiguration("summaryLogFile");
        String unityLogFile = ConfigManager.getConfiguration("unityLogFile");

        System.out.println();
        System.out.println("                                             +==================== CONFIGURATION ====================+");
        System.out.println("                                             |                  Initial Location: " + initLocation + nSpaces(19 - initLocation.length()) + "|");
        System.out.println("                                             |             Maximum Battery Level: " + maxBatteryLevel + nSpaces(19 - maxBatteryLevel.length()) + "|");
        System.out.println("                                             |            Dirt Carrying Capacity: " + dirtCapacity + nSpaces(19 - dirtCapacity.length()) + "|");
        System.out.println("                                             | Charging Station Detection Radius: " + chargingStationDetectionRadius + nSpaces(19 - chargingStationDetectionRadius.length()) + "|");
        System.out.println("                                             |                 Navigator Version: " + navigator + nSpaces(19 - navigator.length()) + "|");
        System.out.println("                                             |    Scheduled Wait (milli seconds): " + scheduledWait + nSpaces(19 - scheduledWait.length()) + "|");
        System.out.println("                                             |      Time in Tile (milli seconds): " + timeInTile + nSpaces(19 - timeInTile.length()) + "|");
        System.out.println("                                             |    Time to Charge (milli seconds): " + timeToCharge + nSpaces(19 - timeToCharge.length()) + "|");
        System.out.println("                                             |                Floor Builder Type: " + floorBuilderType + nSpaces(19 - floorBuilderType.length()) + "|");
        System.out.println("                                             |                   Floor Plan File: " + floorPlan + nSpaces(19 - floorPlan.length()) + "|");
        System.out.println("                                             |               Dirt Generator Type: " + dirtGeneratorType + nSpaces(19 - dirtGeneratorType.length()) + "|");
        System.out.println("                                             |    Logs Home Environment Variable: " + logsHome + nSpaces(19 - logsHome.length()) + "|");
        System.out.println("                                             |                  Summary Log File: " + summaryLogFile + nSpaces(19 - summaryLogFile.length()) + "|");
        System.out.println("                                             |                    Unity Log File: " + unityLogFile + nSpaces(19 - unityLogFile.length()) + "|");
        System.out.println("                                             +=======================================================+");
        System.out.println();
    }
    public static void printSummary(long zeroTime, long currentMillis, int numberOfRuns, String initLocation, String finalLocation, double percentDone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date startTime = new Date(zeroTime);
        String start = formatter.format(startTime);
        Date finishTime = new Date(currentMillis);
        String end = formatter.format(finishTime);
        long duration = currentMillis - zeroTime;
        String percentage = percentDone + "%";
        System.out.println();
        System.out.println("                                             +======================= SUMMARY =======================+");
        System.out.println("                                             |               Initial Location: " + initLocation + nSpaces(22 - initLocation.length()) + "|");
        System.out.println("                                             |                 Final Location: " + finalLocation + nSpaces(22 - finalLocation.length()) + "|");
        System.out.println("                                             |                     Run Number: " + numberOfRuns + nSpaces(22 - (numberOfRuns + "").length()) + "|");
        System.out.println("                                             |                Start Date Time: " + start + nSpaces(22 - start.length()) + "|");
        System.out.println("                                             |               Finish Date Time: " + end + nSpaces(22 - end.length()) + "|");
        System.out.println("                                             |     Time Taken (milli seconds): " + duration + nSpaces(22 - Long.toString(duration).length()) + "|");
        System.out.println("                                             |                Percentage Done: " + percentage + nSpaces(22 - percentage.length()) + "|");
        System.out.println("                                             +=======================================================+");
        System.out.println();
    }
    public static void printStateTransition(String state, long zeroTime) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 11 - state.length(); i++) {
            sb.append("-");
        }
        System.out.println();
        LogManager.print("[" + state + "]  " + sb.toString() + "----  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------", zeroTime);
        System.out.println();
    }
    public static void printDonePercentage(double percentage, long zeroTime) {
        System.out.println("Percentage of Done Tiles = " + percentage + "%");
    }
    public static String formatElapsedTime(long milliTime, long zeroTime) {
        long elapsedTime = milliTime - zeroTime;
        long elapsedSeconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);
        long s = elapsedSeconds % 60;
        long m = ((elapsedSeconds - s) % 3600) / 60;
        long h = (elapsedSeconds - (elapsedSeconds - s) % 3600) / 60;
        //show milliseconds...
        long decimals = (elapsedTime - 3600000 * h - 60000 * m - 1000 * s);
        return String.format("[%02d:%02d:%02d.%03d]  ", h, m, s, decimals);
        //do not show milli seconds
        //return String.format("[%02d:%02d:%02d]  ", h, m, s);
    }
    public static boolean isPrime(int n) {
        for(int i = 2; i <= Math.round(Math.sqrt(n)); i++) {
            if(n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static Location getNeighbor(Location location, Direction direction) {
        int x = location.getX();
        int y = location.getY();
        switch (direction) {
            case NORTH: return LocationFactory.createLocation(x, y - 1);
            case SOUTH: return LocationFactory.createLocation(x, y + 1);
            case EAST: return LocationFactory.createLocation(x + 1, y);
            case WEST: return LocationFactory.createLocation(x - 1, y);
            default: throw new RobotException("Impossible direction. Only N, S, E, W directions are available.");
        }
    }
    public static String nSpaces(int n) {
        if(n < 0) {
            throw new RobotException("Invalid number of white spaces: " + n);
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}