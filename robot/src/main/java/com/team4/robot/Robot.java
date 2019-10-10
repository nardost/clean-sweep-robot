package com.team4.robot;

public class Robot {

    private State state;
    private Location location;

    private Navigator navigator;

    private static Robot robot = null;

    private Robot() throws RobotException {
        setState(State.OFF);
        int x = Integer.parseInt(ConfigManager.getConfiguration("initX"));
        int y = Integer.parseInt(ConfigManager.getConfiguration("initY"));
        setLocation(new Location(x, y));
        setNavigator(new NavigatorAlpha());
    }

    /**
     * Robot has to be a singleton since there should only be one robot.
     *
     * @return Robot
     */
    public static Robot getInstance() throws RobotException {
        if(robot == null) {
            synchronized (Robot.class) {
                if(robot == null) {
                    robot = new Robot();
                }
            }
        }
        return robot;
    }

    State getState() {
        return state;
    }

    void setState(State state) throws RobotException {
        if(state == null) {
            throw new RobotException("Null state is not allowed.");
        }
        this.state = state;
    }

    Location getLocation() {
        return location;
    }

    void setLocation(Location location) throws RobotException {
        if(location == null) {
            throw new RobotException("Null input not allowed.");
        }
        this.location = location;
    }

    Navigator getNavigator() {
        return navigator;
    }

    void setNavigator(Navigator navigator) throws RobotException {
        if(navigator == null) {
            throw new RobotException("Null navigator not allowed.");
        }
        this.navigator = navigator;
    }

    public void turnOn() {
        try {
            setState(State.STANDBY);
        } catch (RobotException re) {
            re.printStackTrace();
        }
    }

    public void turnOff() {
        try {
            setState(State.OFF);
        } catch (RobotException re) {
            re.printStackTrace();
        }
    }

    void gotoChargingStation() throws RobotException {
        setLocation(new Location(0, 0));
    }

    public void traverse() throws RobotException {
        if(getState() != State.OFF) {
            getNavigator().traverseFloor();
        } else {
            System.out.println("TURN ME ON!!!");
        }
    }
}
