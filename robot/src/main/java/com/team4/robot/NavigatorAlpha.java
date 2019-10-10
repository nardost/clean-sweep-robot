package com.team4.robot;

import com.team4.sensor.SensorSimulator;

public class NavigatorAlpha implements Navigator {

    private static final int FLOOR_WIDTH = SensorSimulator.getFloorDimension()[0];
    private static final int FLOOR_LENGTH = SensorSimulator.getFloorDimension()[1];

    @Override
    public void traverseFloor() throws RobotException {
        System.out.println("Robot in " + Robot.getInstance().getState() + " mode.");
        System.out.println("Robot going into " + State.WORKING.toString() + " mode");
        Robot.getInstance().setState(State.WORKING);
        Direction next = Direction.EAST;
        int x = Robot.getInstance().getLocation().getX();
        int y = Robot.getInstance().getLocation().getY();
        for(int j = 0; j < y; j++) {
            System.out.println();
        }
        while(Robot.getInstance().getLocation().getY() < FLOOR_LENGTH - 1) {
            System.out.println("(" + Robot.getInstance().getLocation().getX() + ", " + Robot.getInstance().getLocation().getY()  + ")");
            try {
                Thread.sleep(500L);
                if(Robot.getInstance().getLocation().getY() == FLOOR_LENGTH - 1) {
                    break;
                }
                move(Direction.SOUTH);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        String tabs = "";
        for(int i = 0; i < x; i++) {
            tabs = tabs + "\t";
        }
        System.out.print(tabs);
        while(Robot.getInstance().getLocation().getX() <= FLOOR_WIDTH - 1) {
            System.out.print("(" + Robot.getInstance().getLocation().getX() + ", " + Robot.getInstance().getLocation().getY()  + ")\t");
            try {
                Thread.sleep(500L);
                if(Robot.getInstance().getLocation().getX() == FLOOR_WIDTH - 1) {
                    break;
                }
                move(Direction.EAST);
                tabs = (next == Direction.EAST) ? tabs + "\t" : tabs;
                next = (next == Direction.EAST) ? Direction.SOUTH : Direction.EAST;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        Robot.getInstance().setState(State.STANDBY);
        System.out.println("\nRobot in " + Robot.getInstance().getState() + " mode.");
    }

    void move(Direction direction) throws RobotException {

        int currentX = Robot.getInstance().getLocation().getX();
        int currentY = Robot.getInstance().getLocation().getY();
        switch(direction) {
            case EAST:
                if(currentX == FLOOR_WIDTH - 1) {
                    return;
                }
                Robot.getInstance().setLocation(new Location(currentX + 1, currentY));
                return;
            case WEST:
                if(currentX == 0) {
                    return;
                }
                Robot.getInstance().setLocation(new Location(currentX - 1, currentY));
                return;
            case NORTH:
                if(currentY == 0) {
                    return;
                }
                Robot.getInstance().setLocation(new Location(currentX, currentY - 1));
                return;
            case SOUTH:
                if(currentX == FLOOR_LENGTH) {
                    return;
                }
                Robot.getInstance().setLocation(new Location(currentX, currentY + 1));
                return;
        }
    }
}
