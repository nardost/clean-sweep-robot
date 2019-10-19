package com.team4;

import com.team4.robot.RobotCleanSweep;
import com.team4.robot.Robot;

public class CleanSweepApp {
    public static void main( String[] args ) {
        Robot cleanSweepRobot = RobotCleanSweep.getInstance();
        cleanSweepRobot.turnOn();
        cleanSweepRobot.turnOff();
    }
}
