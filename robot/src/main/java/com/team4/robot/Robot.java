package com.team4.robot;

import com.team4.commons.RobotException;

public interface Robot {
    void turnOn() throws RobotException;
    void turnOff() throws RobotException;
}
