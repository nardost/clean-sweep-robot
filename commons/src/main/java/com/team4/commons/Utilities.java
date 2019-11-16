package com.team4.commons;

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
            System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
            System.out.println("     CLOCK  DIRECTION  LOCATION     BEFORE      AFTER  FLOOR TYPE  BATTERY BEFORE  BATTERY AFTER  DIRT LEVEL               OPEN DIRECTIONS\tCHARGING STATIONS NEARBY");
            System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
        }
    }

    public static void doTimeDelay(long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    public static void doLoopedTimeDelay(String msg, int numberOfLoops, long zeroTime) {
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
    public static void printDonePercentage(double percentage, long zeroTime) {
        System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
        LogManager.print("Percentage of Done Tiles = " + percentage + "%", zeroTime);
        System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
    }
    public static String formatElapsedTime(long milliTime, long zeroTime) {
        long elapsedTime = milliTime - zeroTime;
        long elapsedSeconds = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.MILLISECONDS);
        long s = elapsedSeconds % 60;
        long m = ((elapsedSeconds - s) % 3600) / 60;
        long h = (elapsedSeconds - (elapsedSeconds - s) % 3600) / 60;
        /* to show milliseconds...
        long decimals = (elapsedTime - 3600000 * h - 60000 * m - 1000 * s);
        return String.format("[%02d:%02d:%02d.%3d]  ", h, m, s, decimals);
        */
        return String.format("[%02d:%02d:%02d]  ", h, m, s);
    }
    public static boolean isPrime(int n) {
        for(int i = 2; i <= Math.round(Math.sqrt(n)); i++) {
            if(n % i == 0) {
                return false;
            }
        }
        return true;
    }
}