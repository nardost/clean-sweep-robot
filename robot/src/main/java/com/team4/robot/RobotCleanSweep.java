package com.team4.robot;

import com.team4.commons.*;
import com.team4.sensor.SensorSimulator;
import com.team4.sensor.FloorDao;
import static com.team4.commons.State.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class RobotCleanSweep implements Robot {

    private static long zeroTime;

    private State state;
    private Location location;


    private Navigator navigator;
    private VacuumCleaner vacuumCleaner;
    private PowerManager powerManager;

    //Robot's memory of visited cells
    private HashMap<String, Location> visited = new HashMap<>();
    //unvisited cells
    private Stack<Location> unvisited = new Stack<Location>();
    //graph built as robot progresses
    private HashMap<Location, List<Location>> graph = new HashMap<>();
    //Path class for printing
    private HashMap<Location, DirtUnits> doneTiles = new HashMap<>();

    private static RobotCleanSweep robotCleanSweep = null;

    //options pursued?

    // will delete, just for printing the grid to help programmer
    PrintPath path = new PrintPath(SensorSimulator.getInstance().getFloorDimension()[0],SensorSimulator.getInstance().getFloorDimension()[1]);

    private RobotCleanSweep() {

        setZeroTime(System.currentTimeMillis());
        setState(OFF);
        String locationTuple = ConfigManager.getConfiguration("initLocation");
        int x = Utilities.xFromTupleString(locationTuple);
        int y = Utilities.yFromTupleString(locationTuple);
        setLocation(LocationFactory.createLocation(x, y));
        setNavigator(NavigatorFactory.createNavigator());
        setPowerManager(new PowerUnit());
        setVacuumCleaner(new DirtManager());
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

    @Override
    public void turnOn() {
        Utilities.printLogo();
        setState(STANDBY);
        //Robot waits for cleaning schedule.
        int scheduledWait = Integer.parseInt(ConfigManager.getConfiguration("scheduledWait"));
        Utilities.doLoopedTimeDelay("...WAITING FOR SCHEDULED CLEANING TIME...", scheduledWait, getZeroTime());

        //Robot begins work.
        Mode mode = Mode.VERBOSE;
        Long timeInTile = Long.parseLong(ConfigManager.getConfiguration("timeInTile"));
        work(mode, timeInTile);
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
        System.out.println();
        LogManager.print("...TURNED OFF...", getZeroTime());
        System.out.println();
    }

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

        // -----------------------------------------------------------------------
        if (getState() != OFF) {

            setState(WORKING);

            Utilities.printFormattedHeader(mode);

            while(getState() == WORKING) {

                Utilities.doTimeDelay(timeInTile);

                FloorDao floorDaoBefore = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                createLocations(floorDaoBefore.openPassages);
                buildGraph(getLocation(), floorDaoBefore.openPassages);
                double cost = floorDaoBefore.floorType.getValue();

                Direction direction = getNavigator().traverseFloor(floorDaoBefore.openPassages);

                if(direction != null) {
                    double batteryLevelBefore = getPowerManager().getBatteryLevel();
                    getVacuumCleaner().clean(cost);

                    double batteryLevelAfter = getPowerManager().getBatteryLevel();
                    FloorDao floorDaoAfter = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                    logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, direction, mode);
                    move(direction, cost);
                    //getPowerManager().updateBatteryLevel(cost);

                    //getVacuumCleaner().clean();

                    if(getPowerManager().getBatteryLevel() <= 0) {
                        setState(LOW_BATTERY);
                    }
                } else {
                    setState(STANDBY);
                }

                if(getState() == LOW_BATTERY) {

                    FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                    move(backToCharge(), floorDao.floorType.getValue());
                    //logTileInfo(floorDaoBefore, floorDao, getPowerManager().getBatteryLevel(), direction, mode);

                }
                /*
                if(getState()==FULL_TANK) {
                	System.out.println();
                	System.out.println("DIRT TANK FULL...");
                	System.out.println();
                	try {
                	    Thread.sleep(5000L);
                    } catch (InterruptedException ie) {
                	    ie.printStackTrace();
                    }
                    getVacuumCleaner().emptyTank();
                    System.out.println("...DIRT TANK EMPTY");
                    setState(WORKING);
                    getVacuumCleaner().clean();
                    System.out.println();
                }*/
            }
            //logTileInfo(floorDao, null);
            //System.out.println("Battery Level: " + getPowerManager().getBatteryLevel());
        }
    }


    private void move(Direction direction, double cost) {
        int currentX = RobotCleanSweep.getInstance().getLocation().getX();
        int currentY = RobotCleanSweep.getInstance().getLocation().getY();
        if(direction == null) {
            getPowerManager().updateBatteryLevel(cost);
            return;
            //return floorVal;
        }
        switch(direction) {

            case NORTH:
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY - 1));
                break;

            case SOUTH:
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX, currentY + 1));
                break;

            case WEST:
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX - 1, currentY));
                break;

            case EAST:
                RobotCleanSweep.getInstance().setLocation(LocationFactory.createLocation(currentX + 1, currentY));
                break;
        }

        FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
        cost += floorDao.floorType.getValue();
        cost = cost/2.0;
        getPowerManager().updateBatteryLevel(cost);
        if(getState() == LOW_BATTERY) {

            move(backToCharge(),floorDao.floorType.getValue());

        }
        //return cost;
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
        this.unvisited.remove(location);
        this.visited.put(key, location);
    }

    boolean visitedLocation(Location location) {
        int x = location.getX();
        int y = location.getY();
        String key = Utilities.tupleToString(x,y);
        return this.visited.containsKey(key);
    }

    boolean putUnvisited(Location location) {
        int x = location.getX();
        int y = location.getY();
        String key = Utilities.tupleToString(x,y);
        if(this.visited.containsKey(key)) {

            return true;
        }
        else {
            if(!this.unvisited.contains(location)) {
                this.unvisited.push(location);
            }
            return false;
        }
    }

    boolean visitedAll() {
        return this.unvisited.isEmpty();
    }

    Navigator getNavigator() {
        return navigator;
    }
    Location lastUnvisited() {
        return this.unvisited.pop();
    }

    private void setNavigator(Navigator navigator) {
        if(navigator == null) {
            throw new RobotException("Null navigator object not allowed.");
        }
        this.navigator = navigator;
    }

    PowerManager getPowerManager() {
        return powerManager;
    }

    private void setPowerManager(PowerManager powerManager) {
        if(powerManager == null) {
            throw new RobotException("Null power manager is not allowed.");
        }
        this.powerManager = powerManager;
    }

    
    VacuumCleaner getVacuumCleaner() {
    	
    	return vacuumCleaner;
    	
    }
    
    private void setVacuumCleaner(VacuumCleaner vacuumCleaner) {
    	if (vacuumCleaner == null) {
    		throw new RobotException("Null vacuum cleaner is not allowed.");
    	}
    	this.vacuumCleaner = vacuumCleaner;
    }

    HashMap<String, Location>  getVisited(){
        return this.visited;
    }

    HashMap<Location, List<Location>> getGraph() {
        return graph;
    }

    void setGraph(HashMap<Location, List<Location>> graph) {
        if(graph == null) {
            throw new RobotException("Null graph not allowed.");
        }
        this.graph = graph;

    }

    void createLocations(Direction [] directions) {
        int currentX = RobotCleanSweep.getInstance().getLocation().getX();
        int currentY = RobotCleanSweep.getInstance().getLocation().getY();
        Location location = null;
        for(int i = 0; i < directions.length; i++) {
            switch(directions[i]) {
                case NORTH:
                    location = LocationFactory.createLocation(currentX, currentY - 1);
                    break;
                case SOUTH:
                    location = LocationFactory.createLocation(currentX, currentY + 1);
                    break;
                case WEST:
                    location =  LocationFactory.createLocation(currentX - 1, currentY);
                    break;
                case EAST:
                    location = LocationFactory.createLocation(currentX + 1, currentY);
                    break;
                default:
                    throw new RobotException("Impossible direction. Only N, S, E, W directions are available.");
            }
            putUnvisited(location);
        }
    }

    /**
     * The robot builds a graph of locations it has been to
     * and locations it knows it can go to.
     * @param location
     * @param directions
     */
    private void buildGraph(Location location, Direction [] directions) {
        int x = location.getX();
        int y = location.getY();
        List<Location> neighbors = new ArrayList<>();
        for(Direction direction : directions) {
            neighbors.add(getNeighbor(location, direction));
        }
        getGraph().put(location, neighbors);
    }

    private Location getNeighbor(Location location, Direction direction) {
        int x = location.getX();
        int y = location.getY();
        switch (direction) {
            case NORTH: return LocationFactory.createLocation(x, y - 1);
            case SOUTH: return LocationFactory.createLocation(x, y + 1);
            case EAST: return LocationFactory.createLocation(x + 1, y);
            case WEST: return LocationFactory.createLocation(x - 1, y);
            default: throw new RobotException("Impossible direction. Only N, S, E, W directions are available.");
        }
    }

    Direction backToCharge() {
    	if(RobotCleanSweep.getInstance().getLocation().equals( LocationFactory.createLocation(0, 9))){
        	getPowerManager().recharge();
        	setState(WORKING);
        	return null;
    	}
    	AStar aStar = new AStar(RobotCleanSweep.getInstance().getGraph(),RobotCleanSweep.getInstance().getLocation(),LocationFactory.createLocation(0, 9) ,2);
        return aStar.search().pop();
    }

    private void logTileInfo(FloorDao floorDaoBefore, FloorDao floorDaoAfter, double batteryLevelBefore, double batteryLevelAfter, Direction direction, Mode mode) {

        if(mode == Mode.VERBOSE) {
            StringBuilder sb = new StringBuilder();
            sb.append(Utilities.padSpacesToFront((direction == null) ? "" : direction.toString(), 9));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront("(" + RobotCleanSweep.getInstance().getLocation().getX() + ", " + RobotCleanSweep.getInstance().getLocation().getY() + ")", 8));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront((floorDaoBefore.isClean) ? "CLEAN" : "NOT CLEAN", 9));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront((floorDaoAfter.isClean) ? "CLEAN" : "NOT CLEAN", 9));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront(floorDaoBefore.floorType.toString(), 10));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront(Double.toString(batteryLevelBefore), 14));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront(Double.toString(batteryLevelAfter), 13));
            sb.append("  ");
            sb.append(Utilities.padSpacesToFront(Utilities.arrayToString(floorDaoBefore.openPassages), 28));
            sb.append("\t");
            sb.append(Utilities.arrayToString(floorDaoBefore.chargingStations));
            LogManager.print(sb.toString(), getZeroTime());
        }
    }

    /**
     * For testing purposes.
     */
    void dryRun() {
        work(Mode.SILENT, 0L);
    }
}
