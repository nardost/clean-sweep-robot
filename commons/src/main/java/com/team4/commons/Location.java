package com.team4.commons;

import java.util.Objects;

public class Location {

    private int x;
    private int y;

    private Location() {
    }

    Location(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if( x < 0 ) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if( y < 0) {
            throw new RobotException("Negative coordinates are not allowed in locations.");
        }
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
