package com.team4.robot;

import com.team4.commons.*;
import com.team4.sensor.SensorSimulator;
import com.team4.sensor.FloorDao;
import static com.team4.commons.State.*;
import static com.team4.commons.WorkingMode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RobotCleanSweep implements Robot {

    private static long zeroTime;
    private static int numberOfRuns;
    public static WorkingMode workingMode;

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

    private Location lastLocation;
    private LinkedList<Location> lastLocationList = new LinkedList<>();
    
    //Charging stations locations
    private ArrayList<Location> chargingStations = new ArrayList<>();
    private Location currentChargingStation = null;
    
    private static RobotCleanSweep robotCleanSweep = null;

    private RobotCleanSweep() {
        setZeroTime(System.currentTimeMillis());
        numberOfRuns = 0;
        workingMode = DEPLOYED;
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
        setState(STANDBY);
        //Robot waits for cleaning schedule.
        int scheduledWait = Integer.parseInt(ConfigManager.getConfiguration("scheduledWait"));
        Utilities.doLoopedTimeDelay("Waiting for cleaning schedule at " + getLocation(), scheduledWait, getZeroTime());

        //Robot begins work.
        //WorkingMode mode = DEPLOYED;
        //Long timeInTile = Long.parseLong(ConfigManager.getConfiguration("timeInTile"));
        work();
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
        LogManager.print("Bye!", getZeroTime());
        Utilities.printDonePercentage(SensorSimulator.getInstance().getDonePercentage(), getZeroTime());
    }

    private void work() {
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
            Utilities.printFormattedHeader(workingMode);
            while(getState() == WORKING) {
                Long timeInTile = Long.parseLong(ConfigManager.getConfiguration("timeInTile"));
                if(workingMode != DEPLOYED) {
                    timeInTile = 0L;
                }
                Utilities.doTimeDelay(timeInTile);
                FloorDao floorDaoBefore = SensorSimulator.getInstance().getLocationInfo(getLocation());
                if(floorDaoBefore.isClean) {
                    SensorSimulator.getInstance().setTileDone(location);
                }
                if(floorDaoBefore.chargingStations.length == 1) {
                	setCurrentChargingStation(floorDaoBefore.chargingStations[0]);
                }
                createLocations(floorDaoBefore.openPassages);
                buildGraph(getLocation(), floorDaoBefore.openPassages);
                double cost = floorDaoBefore.floorType.getCost();

                Direction direction = getNavigator().traverseFloor(floorDaoBefore.openPassages);

                if(direction != null ) {
                    double batteryLevelBefore = getPowerManager().getBatteryLevel();
                    while(!(SensorSimulator.getInstance().getLocationInfo(getLocation())).isClean && getState() != LOW_BATTERY) {
                        getVacuumCleaner().clean(cost);
                    }
                    int dirtLevelAfter = getVacuumCleaner().getDirtLevel();
                    double batteryLevelAfter = getPowerManager().getBatteryLevel();
                    FloorDao floorDaoAfter = SensorSimulator.getInstance().getLocationInfo(getLocation());
                    for(Location chargingStation: floorDaoAfter.chargingStations) {
                    	if(!getChargingStations().contains(chargingStation)) {
                    		getChargingStations().add(chargingStation);
                    	}
                    }
                    if(workingMode == DEPLOYED) {
                        logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, dirtLevelAfter, direction);
                    }
                    move(direction, cost);
                }
                else {
                    //duplicate logic. find a way to refactor this.
                    double batteryLevelBefore = getPowerManager().getBatteryLevel();
                    while(!(SensorSimulator.getInstance().getLocationInfo(getLocation())).isClean && getState()!= LOW_BATTERY) {
                        getVacuumCleaner().clean(cost);
                    }
                    int dirtLevelAfter = getVacuumCleaner().getDirtLevel();
                    double batteryLevelAfter = getPowerManager().getBatteryLevel();
                    FloorDao floorDaoAfter = SensorSimulator.getInstance().getLocationInfo(getLocation());
                    if(workingMode == DEPLOYED) {
                        logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, dirtLevelAfter, direction);
                    }
                    setState(STANDBY);
                    updateNumberOfRuns();
                }

                if(getState() == LOW_BATTERY) {
                    FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(getLocation());
                    buildGraph(getLocation(), floorDao.openPassages);
                    getLastLocationList().add(getLocation());
                    move(backToCharge(), floorDao.floorType.getCost());
                }
                
                if(getState() == MOVING_BACK) {
                	FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(getLocation());
                	getLastLocationList().add(getLocation());
                	buildGraph(getLocation(), floorDao.openPassages);
                	move(movingBack(), floorDao.floorType.getCost());
                }
            }
            if(SensorSimulator.getInstance().getDonePercentage() == 100.0) {
                homeToNearestChargingStation();
            }
        }
    }

    private void move(Direction direction, double cost) {

        if(direction == null) {
            /**
             * Do not consume power without moving.
             */
            if(getLocation() == getCurrentChargingStation() || getLocation() == getLastLocation()) {
                cost = 0.0;
            }
            getPowerManager().updateBatteryLevel(cost);
            return;
        }
        
        if(!SensorSimulator.getInstance().getLocationInfo(getLocation()).isClean) {
        	putDirty(getLocation());
        }

        setLocation(Utilities.getNeighbor(getLocation(), direction));
        
        FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(getLocation());
        cost += floorDao.floorType.getCost();
        cost = cost / 2.0;
        buildGraph(getLocation(), floorDao.openPassages);
        getPowerManager().updateBatteryLevel(cost);
        if(getState() == LOW_BATTERY) {
        	getLastLocationList().add(getLocation());
        	move(backToCharge(), floorDao.floorType.getCost());
        }
        if(getState() == MOVING_BACK) {
        	move(movingBack(), floorDao.floorType.getCost());
        }
    }

    long getZeroTime() {
        return zeroTime;
    }

    static int getNumberOfRuns() {
        return numberOfRuns;
    }

    static void updateNumberOfRuns() {
        RobotCleanSweep.numberOfRuns += 1;
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
        if(workingMode == DEPLOYED) {
            Utilities.printStateTransition(getState().toString());
        }
    }
    Location getLastLocation() {
    	return this.lastLocation;
    }
    void setLastLocation(Location l_location) {
    	this.lastLocation = l_location;
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
    
    void putDirty(Location location) {
    	this.unvisited.add(location);
    }

    boolean visitedAll() {
        return this.unvisited.isEmpty();
    }
        Navigator getNavigator() {
        return navigator;
        }
        Location lastUnvisited() {
        return this.unvisited.peek();
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

    HashMap<Location, List<Location>> getGraph() {
        return graph;
    }

    private void createLocations(Direction [] directions) {
        for(int i = 0; i < directions.length; i++) {
            putUnvisited(Utilities.getNeighbor(getLocation(), directions[i]));
        }
    }

    /**
     * The robot builds a graph of locations it has been to
     * and locations it knows it can go to.
     * @param location
     * @param directions
     */
    private void buildGraph(Location location, Direction [] directions) {
        List<Location> neighbors = new ArrayList<>();
        for(Direction direction : directions) {
            neighbors.add(Utilities.getNeighbor(location, direction));
        }
        getGraph().put(location, neighbors);
    }

    Direction backToCharge() {
        if(getLocation().equals(getCurrentChargingStation())){
        	String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
        	String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
        	
        	for(int i = 0; i <= 4 ; i++) {
        		LogManager.logForUnity(getLocation(), "CHARGING", batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
        	}
        	getPowerManager().recharge();
            batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
            LogManager.logForUnity(getLocation(), "CHARGED",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            getLocBeforeCharge();
            setState(MOVING_BACK);
            return null;
        }
        AStar aStar = new AStar(getGraph(), getLocation(), getCurrentChargingStation() ,2);
        Direction direction = null;
        if(aStar.search() != null && !aStar.search().empty()) {
            direction = aStar.search().pop();
        }
        return direction;
    }
    
    Location getLocBeforeCharge() {
    	setLastLocation(getLastLocationList().removeFirst());
    	getLastLocationList().clear();
    	return getLastLocation();
    }

    Direction movingBack() {
    	if(getLocation().equals( getLastLocation())) {
        	String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
        	String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
        	LogManager.logForUnity(getLocation(), "RESUME",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            setState(WORKING);
    		return null;
    	}
        AStar aStar = new AStar(getGraph(), getLocation(), getLastLocation() ,2);
    	String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
    	String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
    	LogManager.logForUnity(getLocation(), "GO_LAST",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
        FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
        if(workingMode == DEPLOYED) {
            RobotCleanSweep.getInstance().logTileInfo(
                    floorDao,
                    floorDao,
                    getPowerManager().getBatteryLevel(),
                    getVacuumCleaner().getDirtLevel(),
                    getVacuumCleaner().getDirtLevel(),
                    null);
        }
    	return aStar.search().pop();
    }

    /**
     * Homing feature. Return to the nearest charging station after completing work.
     */
    private void homeToNearestChargingStation() {
        while(!(getLocation().equals(getCurrentChargingStation()))) {
            FloorDao floorDaoBefore = SensorSimulator.getInstance().getLocationInfo(getLocation());
            double batteryLevelBefore = getPowerManager().getBatteryLevel();
            move(backToCharge(), floorDaoBefore.floorType.getCost());
            FloorDao floorDaoAfter = SensorSimulator.getInstance().getLocationInfo(getLocation());
            int dirtLevelAfter = getVacuumCleaner().getDirtLevel();
            double batteryLevelAfter = getPowerManager().getBatteryLevel();
            if(workingMode == DEPLOYED) {
                logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, dirtLevelAfter, null);
            }
        }
        if((getLocation().equals(getCurrentChargingStation()))) {
            String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
            String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());

            for(int i = 0; i <= 4 ; i++) {
                LogManager.logForUnity(getLocation(), "CHARGING", batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            }
            getPowerManager().recharge();
            batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
            if(workingMode == DEPLOYED) {
                LogManager.print("Battery recharged. Battery level: " + getPowerManager().getBatteryLevel(), getZeroTime());
            }
            LogManager.logForUnity(getLocation(), "CHARGED",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
        }
        updateNumberOfRuns();
    }

    void logTileInfo(FloorDao floorDaoBefore, FloorDao floorDaoAfter, double batteryLevelBefore, double batteryLevelAfter, int dirtLevelAfter, Direction direction) {
        StringBuilder simple = new StringBuilder();
        simple.append(Utilities.padSpacesToFront("pos" + "(" + getLocation().getX() + ", " + getLocation().getY() + ")", 8));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorDaoBefore.isClean) ? "act[ALREADY_CLEAN] " : "", 9));
        simple.append(" ");
        simple.append(Utilities.padSpacesToFront((floorDaoAfter.isClean && floorDaoBefore.isClean) ? "act[CLEAN]" : "", 9));
        LogManager.logForUnity(getLocation(), floorDaoBefore.isClean, floorDaoAfter.isClean,Double.toString(batteryLevelAfter), Integer.toString(dirtLevelAfter), getNumberOfRuns());

        StringBuilder sb = new StringBuilder();
        sb.append(Utilities.padSpacesToFront((direction == null) ? "" : direction.toString(), 9));
        sb.append("  ");
        sb.append(Utilities.padSpacesToFront("(" + getLocation().getX() + ", " + getLocation().getY() + ")", 8));
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
        sb.append(Utilities.padSpacesToFront(Integer.toString(dirtLevelAfter), 10));
        sb.append("  ");
        sb.append(Utilities.padSpacesToFront(Utilities.arrayToString(floorDaoBefore.openPassages), 28));
        sb.append("\t");
        sb.append(Utilities.arrayToString(floorDaoBefore.chargingStations));
        LogManager.print(sb.toString(), getZeroTime());
    }

    /**
     * For testing purposes.
     */
    void dryRun() {
        workingMode = TESTING;
        work();
    }

	public LinkedList<Location> getLastLocationList() {
		return lastLocationList;
	}

	public ArrayList<Location> getChargingStations() {
		return chargingStations;
	}

	public Location getCurrentChargingStation() {
		return currentChargingStation;
	}

	public void setCurrentChargingStation(Location currentChargingStation) {
		this.currentChargingStation = currentChargingStation;
	}
}
