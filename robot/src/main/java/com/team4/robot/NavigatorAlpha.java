package com.team4.robot;

import com.team4.commons.Direction;
import com.team4.commons.LocationFactory;
import com.team4.commons.RobotException;
import com.team4.sensor.SensorSimulator;
import static com.team4.commons.Direction.*;
import static com.team4.commons.State.*;

class NavigatorAlpha implements Navigator {

    @Override
    public void traverseFloor() throws RobotException {
        final int FLOOR_WIDTH = SensorSimulator.getInstance().getFloorDimension()[0];
        final int FLOOR_LENGTH = SensorSimulator.getInstance().getFloorDimension()[1];
        System.out.println("Robot in " + RobotCleanSweep.getInstance().getState() + " mode.");
        System.out.println("Robot going into " + WORKING.toString() + " mode");
        RobotCleanSweep.getInstance().setState(WORKING);
        Direction next = EAST;
        int x = RobotCleanSweep.getInstance().getLocation().getX();
        int y = RobotCleanSweep.getInstance().getLocation().getY();
        for(int j = 0; j < y; j++) {
            System.out.println();
        }
        while(RobotCleanSweep.getInstance().getLocation().getY() < FLOOR_LENGTH - 1) {
            System.out.println("(" + RobotCleanSweep.getInstance().getLocation().getX() + ", " + RobotCleanSweep.getInstance().getLocation().getY()  + ")");
            try {
                Thread.sleep(500L);
                if(RobotCleanSweep.getInstance().getLocation().getY() == FLOOR_LENGTH - 1) {
                    break;
                }
                move(SOUTH);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        String tabs = "";
        for(int i = 0; i < x; i++) {
            tabs = tabs + "\t";
        }
        System.out.print(tabs);
        while(RobotCleanSweep.getInstance().getLocation().getX() <= FLOOR_WIDTH - 1) {
            System.out.print("(" + RobotCleanSweep.getInstance().getLocation().getX() + ", " + RobotCleanSweep.getInstance().getLocation().getY()  + ")\t");
            try {
                Thread.sleep(500L);
                if(RobotCleanSweep.getInstance().getLocation().getX() == FLOOR_WIDTH - 1) {
                    break;
                }
                move(EAST);
                tabs = (next == EAST) ? tabs + "\t" : tabs;
                next = (next == EAST) ? SOUTH : EAST;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        RobotCleanSweep.getInstance().setState(STANDBY);
        System.out.println("\nRobot in " + RobotCleanSweep.getInstance().getState() + " mode.");
    }

    void move(Direction direction) throws RobotException {
        final int FLOOR_WIDTH = SensorSimulator.getInstance().getFloorDimension()[0];
        final int FLOOR_LENGTH = SensorSimulator.getInstance().getFloorDimension()[1];

        int currentX = RobotCleanSweep.getInstance().getLocation().getX();
        int currentY = RobotCleanSweep.getInstance().getLocation().getY();
        switch(direction) {
            case EAST:
                if(currentX == FLOOR_WIDTH - 1) {
                    return;
                }
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX + 1, currentY));
                return;
            case WEST:
                if(currentX == 0) {
                    return;
                }
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX - 1, currentY));
                return;
            case NORTH:
                if(currentY == 0) {
                    return;
                }
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY - 1));
                return;
            case SOUTH:
                if(currentX == FLOOR_LENGTH) {
                    return;
                }
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY + 1));
                return;
        }
    }
}
