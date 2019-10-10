package com.team4.robot;

class Location {

    private int x;
    private int y;

    private Location() {
    }

    Location(int x, int y) throws RobotException {
        setX(x);
        setY(y);
    }

    int getX() {
        return x;
    }

    void setX(int x) throws RobotException {
        if( x < 0 ) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.x = x;
    }

    int getY() {
        return y;
    }

    void setY(int y) throws RobotException {
        if( y < 0) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.y = y;
    }
}
