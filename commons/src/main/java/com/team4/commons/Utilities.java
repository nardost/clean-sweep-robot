package com.team4.commons;

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

    public static void printFormattedHeader(Mode mode) {
        if(mode == Mode.VERBOSE) {
            System.out.println("----------  ---------  --------  ---------  ---------  ----------\t----------------------------\t------------------------");
            System.out.println("     CLOCK  DIRECTION  LOCATION     BEFORE      AFTER  FLOOR TYPE\t             OPEN DIRECTIONS\tCHARGING STATIONS NEARBY");
            System.out.println("----------  ---------  --------  ---------  ---------  ----------\t----------------------------\t------------------------");
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
            System.out.println();
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
        System.out.println("+=======================================================+");
        System.out.println("|                     CLEAN SWEEP ROBOT                 |");
        System.out.println("+=======================================================+");
        System.out.println();
    }

}
