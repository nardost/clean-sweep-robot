package com.team4.robot;

import com.team4.commons.*;
import com.team4.sensor.SensorSimulator;
import com.team4.sensor.FloorDao;
import static com.team4.commons.State.*;

import java.util.HashMap;

public class RobotCleanSweep implements Robot {

    private static long zeroTime;

    private State state;
    private Location location;

    private Navigator navigator;
    private VacuumCleaner vacuumCleaner;
    private PowerManager powerManager;
    
    //Robot's memory of visited cells
    private HashMap<String, Location> visited;
    
    //Path class for printing
    private HashMap<Location, DirtUnits> doneTiles = new HashMap<>();

    private static RobotCleanSweep robotCleanSweep = null;
    
    private RobotCleanSweep() {

        setZeroTime(System.currentTimeMillis());
        setState(OFF);
        String locationTuple = ConfigManager.getConfiguration("initLocation");
        int x = Utilities.xFromTupleString(locationTuple);
        int y = Utilities.yFromTupleString(locationTuple);
        this.visited = new HashMap<>();
        setLocation(LocationFactory.createLocation(x, y));
        setNavigator(NavigatorFactory.createNavigator());
    }

    /**
     * Robot has to be a singleton since there should only be one robot.
     *
     * @return Robot
     */
    public static RobotCleanSweep getInstance() {
        if(robotCleanSweep == null) {
            synchronized (RobotCleanSweep.class) {
                if(robotCleanSweep == null) {
                    robotCleanSweep = new RobotCleanSweep();
                }
            }
        }
        return robotCleanSweep;
    }

    long getZeroTime() {
        return zeroTime;
    }

    private static void setZeroTime(long zeroTime) {
        if(zeroTime < 0) {
            throw new RobotException("Negative time millis is not allowed.");
        }
        RobotCleanSweep.zeroTime = zeroTime;
    }

    State getState() {
        return state;
    }

    void setState(State state) {
        if(state == null) {
            throw new RobotException("Null state is not allowed.");
        }
        this.state = state;
    }

    Location getLocation() {
        return location;
    }

    void setLocation(Location location) {
        if(location == null) {
            throw new RobotException("Null location object not allowed.");
        }
        String key = Utilities.tupleToString(location.getX(), location.getY());
        this.location = location;
        this.visited.put(key, location);
    }

    boolean visitedLocation(Location location) {
        int x = location.getX();
        int y = location.getY();
    	String key = Utilities.tupleToString(x,y);
    	return this.visited.containsKey(key);
    }

    Navigator getNavigator() {
        return navigator;
    }

    void setNavigator(Navigator navigator) {
        if(navigator == null) {
            throw new RobotException("Null navigator object not allowed.");
        }
        this.navigator = navigator;
    }

    @Override
    public void turnOn() {
        setState(STANDBY);
        //Robot waits for cleaning schedule.
        System.out.println();
        System.out.println("+=======================================================+");
        System.out.println("|                     CLEAN SWEEP ROBOT                 |");
        System.out.println("+=======================================================+");
        System.out.println();
        try {
            int scheduledWait = Integer.parseInt(ConfigManager.getConfiguration("scheduledWait"));
            for(int i = 1; i <= scheduledWait; i++) {
                LogManager.print("waiting for scheduled cleaning time...", getZeroTime());
                Thread.sleep(1000L);
            }
            System.out.println();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        //Robot begins work.
        Mode mode = Mode.VERBOSE;
        Long timeInTile = Long.parseLong(ConfigManager.getConfiguration("timeInTile"));
        work(mode, timeInTile);
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
    }

    private enum Mode { SILENT, VERBOSE };

    private void work(Mode mode, long timeInTile) {
        /** //check if there is enough battery to make it to the nearest charging station
         *  //if(there is enough battery to make it to the nearest charging station) {
         *  //    continue working... ?
         *  //} else {
         *  //    set state low battery... etc..
         *  //}
         *  while(mode is STANDBY && there are tiles not yet visited) {
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
         *              decide where to go next <- the traversal algorithm picks which Tile is next. (navigator)
         *          }
         *          go to next tile (move)
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
        if (getState() != OFF) {
            setState(WORKING);
            System.out.println("begin working...\n");
            FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
            if(mode == Mode.VERBOSE) {
                System.out.println("            DIRECTION  LOCATION       DIRT  FLOOR TYPE\t             OPEN DIRECTIONS\tCHARGING STATIONS NEARBY");
                System.out.println("            ---------  --------  ---------  ----------\t----------------------------\t--------------------------------------------------------------------------------------------------------");
            }
            if(mode == Mode.VERBOSE) {
                logTileInfo(floorDao, null);
            }
            while(getState() == WORKING) {
                try {
                    //add delay to simulate Robot staying in a tile while working.
                    Thread.sleep(timeInTile * 1000L);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                Direction direction = getNavigator().traverseFloor(floorDao.openPassages);
                if(direction != null) {
                    move(direction);
                    if(mode == Mode.VERBOSE) {
                        logTileInfo(floorDao, direction);
                    }
                    //do some work and mark tile as done
                    SensorSimulator.getInstance().setTileDone(getLocation());
                }
                else {
                    setState(STANDBY);
                }
            }
        } else {
            System.out.println("TURN ME ON!!!");
        }
    }

    boolean move(Direction direction) {

        final int FLOOR_WIDTH = SensorSimulator.getInstance().getFloorDimension()[0];
        final int FLOOR_LENGTH = SensorSimulator.getInstance().getFloorDimension()[1];

        int currentX = RobotCleanSweep.getInstance().getLocation().getX();
        int currentY = RobotCleanSweep.getInstance().getLocation().getY();

        switch(direction) {

           case NORTH:
               if(currentY == 0) {
                   System.out.println("WALL!");
                   return false;
               }
               RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY - 1));
               return true;

           case SOUTH:
               if(currentY == FLOOR_LENGTH - 1) {
                   System.out.println("WALL!");
                   return false;
               }
               RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY + 1));
               return true;

           case WEST:
              if(currentX == 0) {
                  System.out.println("WALL!");
                  return false;
              }
              RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX - 1, currentY));
              return true;

           case EAST:
              if(currentX == FLOOR_WIDTH - 1) {
                  System.out.println("WALL!");
                  return false;
              }
              RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX + 1, currentY));
              return true;
        }
        return false;
    }

    private void markTileDone(Location location) {
        if(location == null) {
            throw new RobotException("Null location is not allowed.");
        }
        SensorSimulator.getInstance().setTileDone(location);
    }

    private void logTileInfo(FloorDao floorDao, Direction direction) {

        StringBuilder sb = new StringBuilder();
        sb.append(Utilities.padSpacesToFront( (direction == null) ? "" : direction.toString(), 9));
        sb.append("  ");
        sb.append(Utilities.padSpacesToFront("(" + RobotCleanSweep.getInstance().getLocation().getX() + ", " + RobotCleanSweep.getInstance().getLocation().getY() + ")", 8));
        sb.append("  ");
        sb.append( Utilities.padSpacesToFront((floorDao.isClean) ? "CLEAN" : "NOT CLEAN", 9));
        sb.append("  ");
        sb.append(Utilities.padSpacesToFront(floorDao.floorType.toString(), 10));
        sb.append('\t');
        sb.append( Utilities.padSpacesToFront(Utilities.arrayToString(floorDao.openPassages), 28) );
        sb.append("\t");
        sb.append(Utilities.arrayToString(floorDao.chargingStations));
        LogManager.print(sb.toString() , getZeroTime());
    }

    /**
     * For testing purposes.
     */
    void dryRun() {
        work(Mode.SILENT, 0L);
    }
}
