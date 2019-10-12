package com.team4.robot;

import com.team4.commons.*;

import static com.team4.commons.State.*;

public class RobotCleanSweep implements Robot {

    private State state;
    private Location location;

    private Navigator navigator;
    private VacuumCleaner cleaner;
    private PowerManager powerManager;

    private static RobotCleanSweep robotCleanSweep = null;
    private RobotCleanSweep() throws RobotException {
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
    public static RobotCleanSweep getInstance() throws RobotException {
        if(robotCleanSweep == null) {
            synchronized (RobotCleanSweep.class) {
                if(robotCleanSweep == null) {
                    robotCleanSweep = new RobotCleanSweep();
                }
            }
        }
        return robotCleanSweep;
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
            throw new RobotException("Null location object not allowed.");
        }
        this.location = location;
    }

    Navigator getNavigator() {
        return navigator;
    }

    void setNavigator(Navigator navigator) throws RobotException {
        if(navigator == null) {
            throw new RobotException("Null navigator object not allowed.");
        }
        this.navigator = navigator;
    }

    @Override
    public void turnOn() throws RobotException {
        setState(STANDBY);
        //Robot waits for cleaning schedule.
        System.out.println("Waiting for scheduled cleaning time...");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        //Robot begins work.
        work();
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
    }

    private void work() {
        /** while(mode is STANDBY && there are tiles not yet visited) {
         *      if(dirt tank is full) {
         *          save current tile as the last tile not done
         *          change mode to FULL_TANK
         *          wait until owner empties tank (infinite loop?)
         *          if(tank emptied) {
         *              change mode to STANDBY
         *          }
         *      } else {
         *          if(there is undone tile saved) {
         *              get saved undone tile and make it my next tile
         *          } else {
         *              ask navigator what my next tile is
         *          }
         *          go to next tile
         *          ask sensor simulator information about current tile and save info
         *          if(tile is not clean) {
         *              check if there is enough battery to clean tile
         *              if(there is not enough battery) {
         *                  save current tile as the last tile not done
         *                  change mode to CHARGING
         *                  go to nearest charging station
         *                  charge to 100%
         *                  go back to last tile
         *              } else {
         *                  clean tile
         *              }
         *          }
         *          report battery usage to power manager
         *          save tile to done list
         *      }
         *  }
        */
        try {
            if (getState() != OFF) {
                getNavigator().traverseFloor();
            } else {
                System.out.println("TURN ME ON!!!");
            }
        } catch (RobotException re) {
            re.printStackTrace();
        }
    }
}
