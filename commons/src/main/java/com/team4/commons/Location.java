package com.team4.commons;

public class Location {

    private int x;
    private int y;

    private Location() {
    }

    public Location(int x, int y) throws RobotException {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) throws RobotException {
        if( x < 0 ) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) throws RobotException {
        if( y < 0) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.y = y;
    }
}
