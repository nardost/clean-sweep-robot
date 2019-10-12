package com.team4.robot;

import com.team4.commons.*;

import static com.team4.commons.State.*;

public class Robot {

    private State state;
    private Location location;

    private Navigator navigator;
    private VacuumCleaner cleaner;
    private PowerManager powerManager;

    private static Robot robot = null;
    private Robot() throws RobotException {
        setState(OFF);
        String locationTuple = ConfigManager.getConfiguration("initLocation");
        int x = Utilities.xFromTupleString(locationTuple);
        int y = Utilities.yFromTupleString(locationTuple);
        setLocation(LocationFactory.createLocation(x, y));
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
            setState(STANDBY);
        } catch (RobotException re) {
            re.printStackTrace();
        }
    }

    public void turnOff() {
        try {
            setState(OFF);
        } catch (RobotException re) {
            re.printStackTrace();
        }
    }

    void gotoChargingStation() throws RobotException {
        setLocation(LocationFactory.createLocation(0, 0));
    }

    public void traverse() throws RobotException {
        if(getState() != OFF) {
            getNavigator().traverseFloor();
        } else {
            System.out.println("TURN ME ON!!!");
        }
    }
}
