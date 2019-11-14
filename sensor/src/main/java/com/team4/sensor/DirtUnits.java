package com.team4.sensor;

import com.team4.commons.RobotException;

public enum DirtUnits {
    NONE,
    ONE,
    TWO,
    THREE;

    public int toInt() {
        switch (this) {
            case NONE: return 0;
            case ONE: return 1;
            case TWO: return 2;
            case THREE: return 3;
            default: throw new RobotException("Invalid Dirt Unit");
        }
    }
}
