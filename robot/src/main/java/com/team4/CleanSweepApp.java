package com.team4;

import com.team4.robot.RobotException;
import com.team4.robot.Robot;

public class CleanSweepApp
{
    public static void main( String[] args )
    {
        try {
            Robot r = Robot.getInstance();
            r.turnOn();
            r.traverse();
        } catch (RobotException re) {
            System.out.println(re.getMessage());
        }
    }
}
