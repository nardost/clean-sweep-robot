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
}
