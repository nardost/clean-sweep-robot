package com.team4.robot;

import com.team4.commons.*;
import com.team4.sensor.DirtUnits;
import com.team4.sensor.SensorSimulator;
import com.team4.sensor.FloorDao;
import static com.team4.commons.State.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RobotCleanSweep implements Robot {

    private static long zeroTime;
    private static int numberOfRuns;

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
    
    private Location lastLocation;
    private LinkedList<Location> lastLocationList = new LinkedList<>();
    
    //Charging stations locations
    
    private ArrayList<Location> chargingStations = new ArrayList<Location>();
    private Location currentChargingStation = null;
    
    private static RobotCleanSweep robotCleanSweep = null;
    

    private RobotCleanSweep() {
        setZeroTime(System.currentTimeMillis());
        numberOfRuns = 0;
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
        Utilities.doLoopedTimeDelay("...WAITING FOR SCHEDULED CLEANING TIME AT LOCATION " + getLocation(), scheduledWait, getZeroTime());

        //Robot begins work.
        WorkingMode mode = WorkingMode.DEPLOYED;
        Long timeInTile = Long.parseLong(ConfigManager.getConfiguration("timeInTile"));
        work(mode, timeInTile);
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
        LogManager.print("...TURNED OFF...", getZeroTime());
    }

    private void work(WorkingMode mode, long timeInTile) {
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

                FloorDao floorDaoBefore = SensorSimulator.getInstance().getLocationInfo(getLocation());
                if(floorDaoBefore.chargingStations.length == 1) {
                	
                	RobotCleanSweep.getInstance().setCurrentChargingStation(floorDaoBefore.chargingStations[0]);
                }
                createLocations(floorDaoBefore.openPassages);
                buildGraph(getLocation(), floorDaoBefore.openPassages);
                double cost = floorDaoBefore.floorType.getCost();

                Direction direction = getNavigator().traverseFloor(floorDaoBefore.openPassages);

                if(direction != null) {
                    double batteryLevelBefore = getPowerManager().getBatteryLevel();
                    while(!(SensorSimulator.getInstance().getLocationInfo(getLocation())).isClean) {
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
                    
                    logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, dirtLevelAfter, direction, mode);
                    
                    move(direction, cost);
                } else {
                    //duplicate logic. find a way to refactor this.
                    double batteryLevelBefore = getPowerManager().getBatteryLevel();
                    while(!(SensorSimulator.getInstance().getLocationInfo(getLocation())).isClean) {
                        getVacuumCleaner().clean(cost);
                    }
                    int dirtLevelAfter = getVacuumCleaner().getDirtLevel();
                    double batteryLevelAfter = getPowerManager().getBatteryLevel();
                    FloorDao floorDaoAfter = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                    logTileInfo(floorDaoBefore, floorDaoAfter, batteryLevelBefore, batteryLevelAfter, dirtLevelAfter, direction, mode);
                    setState(STANDBY);
                    updateNumberOfRuns();
                }

                if(getState() == LOW_BATTERY) {
                    FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                    buildGraph(getLocation(), floorDao.openPassages);
                    RobotCleanSweep.getInstance().getLastLocationList().add(RobotCleanSweep.getInstance().getLocation());
                    move(backToCharge(), floorDao.floorType.getCost());
                }
                
                if(getState() == MOVING_BACK) {
                	FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
                	RobotCleanSweep.getInstance().getLastLocationList().add(RobotCleanSweep.getInstance().getLocation());
                	buildGraph(getLocation(), floorDao.openPassages);
                	move(movingBack(), floorDao.floorType.getCost());
                }
            }
        }
    }

    private void move(Direction direction, double cost) {
        int currentX = RobotCleanSweep.getInstance().getLocation().getX();
        int currentY = RobotCleanSweep.getInstance().getLocation().getY();

        if(direction == null) {
            /**
             * Do not consume power right after recharging or
             * right beforewithout moving any where.
             */
            if(getLocation() == getCurrentChargingStation() || getLocation() == getLastLocation()) {
                cost = 0.0;
            }
            getPowerManager().updateBatteryLevel(cost);
            return;
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
        
        removeUnvisited(RobotCleanSweep.getInstance().getLocation());
        FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
        cost += floorDao.floorType.getCost();
        cost = cost / 2.0;
        buildGraph(getLocation(), floorDao.openPassages);
        getPowerManager().updateBatteryLevel(cost);
        if(getState() == LOW_BATTERY) {
        	RobotCleanSweep.getInstance().getLastLocationList().add(RobotCleanSweep.getInstance().getLocation());
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
        if(getState() == STANDBY) {
            System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
            LogManager.print("PERCENTAGE OF DONE TILES = " + SensorSimulator.getInstance().getDonePercentage() + "%", getZeroTime());
            System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
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

    boolean visitedAll() {
        return this.unvisited.isEmpty();
    }
        Navigator getNavigator() {
        return navigator;
        }
        Location lastUnvisited() {
        return this.unvisited.peek();
    }
    
    void removeUnvisited(Location location){
    	this.unvisited.remove(location);
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

        Location location;
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
        if(getLocation().equals(getCurrentChargingStation())){
        	LogManager.print("...AT CHARGING STATION " + getLocation().toString() + " WITH BATTERY LEVEL "+ getPowerManager().getBatteryLevel(), getZeroTime());
        	String dirtLevel = Integer.toString(RobotCleanSweep.getInstance().getVacuumCleaner().getDirtLevel());
        	String batteryLevel = Double.toString(RobotCleanSweep.getInstance().getPowerManager().getBatteryLevel());
        	
        	for(int i =0; i<=4 ; i++) {
        		LogManager.logForUnity(RobotCleanSweep.getInstance().getLocation(), "CHARGING",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
        	}
        	//LogManager.logForUnity(RobotCleanSweep.getInstance().getLocation(), "CHARGING",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            getPowerManager().recharge();
            batteryLevel = Double.toString(RobotCleanSweep.getInstance().getPowerManager().getBatteryLevel());
            LogManager.print("...BATTERY RECHARGED. NEW BATTERY LEVEL: " + getPowerManager().getBatteryLevel(), getZeroTime());
            LogManager.logForUnity(getLocation(), "CHARGED",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            getLocBeforeCharge();
            setState(MOVING_BACK);
            return null;
        }
        AStar aStar = new AStar(getGraph(), getLocation(), getCurrentChargingStation() ,2);
        Direction direction = aStar.search().pop();
        return direction;
    }
    
    Location getLocBeforeCharge(){
    	setLastLocation(getLastLocationList().removeFirst());
    	getLastLocationList().clear();
    	return getLastLocation();
    }

    Direction movingBack() {
    	
    	if(getLocation() == getLastLocation()) {
        	String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
        	String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
        	LogManager.logForUnity(getLocation(), "RESUME",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
            System.out.println("----------  ---------  --------  ---------  ---------  ----------  --------------  -------------  ----------  ----------------------------\t------------------------");
    		setState(WORKING);
    		return null;
    	}
        AStar aStar = new AStar(getGraph(), getLocation(), getLastLocation() ,2);
    	String dirtLevel = Integer.toString(getVacuumCleaner().getDirtLevel());
    	String batteryLevel = Double.toString(getPowerManager().getBatteryLevel());
    	LogManager.logForUnity(getLocation(), "GO_LAST",batteryLevel , dirtLevel, RobotCleanSweep.getNumberOfRuns());
    	LogManager.print("...GOING BACK TO LAST LOCATION. NOW AT " + getLocation().toString() + "  Battery Level: " + getPowerManager().getBatteryLevel(), getZeroTime());
        return aStar.search().pop();
    }

    private void logTileInfo(FloorDao floorDaoBefore, FloorDao floorDaoAfter, double batteryLevelBefore, double batteryLevelAfter, int dirtLevelAfter, Direction direction, WorkingMode mode) {

        if(mode == WorkingMode.DEPLOYED) {
        	StringBuilder simple = new StringBuilder();
            simple.append(Utilities.padSpacesToFront("pos" + "(" + getLocation().getX() + ", " + getLocation().getY() + ")", 8));
            simple.append(" ");
            simple.append(Utilities.padSpacesToFront((floorDaoBefore.isClean) ? "act[ALREADY_CLEAN] " : "", 9));
            simple.append(" ");
            simple.append(Utilities.padSpacesToFront((floorDaoAfter.isClean && floorDaoBefore.isClean) ? "act[CLEAN]" : "", 9));
            LogManager.logForUnity(getLocation(), floorDaoBefore.isClean, floorDaoAfter.isClean,Double.toString(batteryLevelAfter), Integer.toString(dirtLevelAfter), getNumberOfRuns());
        	
        	//for console output
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
    }

    /**
     * For testing purposes.
     */
    void dryRun() {
        work(WorkingMode.TESTING, 0L);
    }

	public LinkedList<Location> getLastLocationList() {
		return lastLocationList;
	}

	public void setLastLocationList(LinkedList<Location> lastLocation) {
		this.lastLocationList = lastLocation;
	}

	public ArrayList<Location> getChargingStations() {
		return chargingStations;
	}

	public void setChargingStations(ArrayList<Location> chargingStations) {
		this.chargingStations = chargingStations;
	}

	public Location getCurrentChargingStation() {
		return currentChargingStation;
	}

	public void setCurrentChargingStation(Location currentChargingStation) {
		this.currentChargingStation = currentChargingStation;
	}
}
