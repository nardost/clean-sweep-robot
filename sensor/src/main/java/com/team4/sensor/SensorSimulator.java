package com.team4.sensor;

import com.team4.commons.*;
import java.util.Arrays;
import static com.team4.commons.Direction.*;

public class SensorSimulator implements Sensor {

    private static Floor floor;

    private static SensorSimulator simulator = null;
    private SensorSimulator() {
        this.floor = Floor.getInstance();
    }

    /**
     * SensorSimulator is a singleton since there is only one simulator per robot.
     *
     * @return SensorSimulator
     */
    public static SensorSimulator getInstance() {
        if(simulator == null) {
            synchronized (SensorSimulator.class) {
                if(simulator == null) {
                    simulator = new SensorSimulator();
                }
            }
        }
        return simulator;
    }

    static Floor getFloor() {
        return floor;
    }

    @Override
    public FloorDao getLocationInfo(Location location) {
        Tile tile = getFloor().getTile(location);
        if(tile == null) {
            throw new RobotException("There is no such location: " + location.toString());
        }
        FloorDao dao = new FloorDao();
        dao.floorType = tile.getFloorType();
        Direction [] directions = new Direction[4];
        int index = 0;
        if(tile.isNorthOpen()) {
            directions[index++] = NORTH;
        }
        if(tile.isSouthOpen()) {
            directions[index++] = SOUTH;
        }
        if(tile.isEastOpen()) {
            directions[index++] = EAST;
        }
        if(tile.isWestOpen()) {
            directions[index++] = WEST;
        }
        dao.openPassages = Arrays.copyOf(directions, index);
        dao.chargingStations = getNeighborsWithinChargingStationDetectionRadius(tile.getLocation());
        return dao;
    }

    public static int getNumberOfTiles() {
        return getFloor().getTiles().size();
    }

    public static int [] getFloorDimension() {
        return new int[] { getFloor().WIDTH, getFloor().LENGTH };
    }

    private int getNumberOfNeighborsWithinRadius(int radius) {
        if(radius < 1) {
            throw new RobotException("A neighboring cell is at least 1 cell away.");
        }
        switch(radius) {
            case 1:
                return 4;
            case 2:
            default:
                return 12;
        }
    }

    private Location [] getNeighborsWithinChargingStationDetectionRadius(Location location) {
        int chargingStationDetectionRadius = Integer.parseInt(ConfigManager.getConfiguration("chargingStationDetectionRadius"));
        switch(chargingStationDetectionRadius) {
            case 1:
                return neighborsWithinRadiusOf1(location);
            case 2:
            default:
                return neighborsWithinRadiusOf2(location);
        }
    }
    private Location [] neighborsWithinRadiusOf1(Location location) {
        final int WIDTH = 1000; //get WIDTH of Wall
        final int LENGTH = 1000; //get LENGTH of Wall
        final int NUMBER_OF_NEIGHBORS_WITHIN_RADIUS = getNumberOfNeighborsWithinRadius(1);
        Location [] neighbors = new Location[NUMBER_OF_NEIGHBORS_WITHIN_RADIUS];
        int index = 0;
        //(x, y-1)
        if(location.getY() >= 1) {
            neighbors[index++] = LocationFactory.createLocation(location.getX(), location.getY() - 1);
        }
        //(x, y+1)
        if(location.getY() <= LENGTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(location.getX(), location.getY() + 1);
        }
        //(x-1, y)
        if(location.getX() >= 1) {
            neighbors[index++] = LocationFactory.createLocation(location.getX() - 1, location.getY());
        }
        //(x+1, y)
        if(location.getX() <= WIDTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(location.getX() + 1, location.getY());
        }
        return Arrays.copyOf(neighbors, index);
    }
    private Location [] neighborsWithinRadiusOf2(Location location) {
        int x = location.getX();
        int y = location.getY();
        final int WIDTH = Integer.parseInt(ConfigManager.getConfiguration("floorWidth"));
        final int LENGTH = Integer.parseInt(ConfigManager.getConfiguration("floorLength"));
        final int NUMBER_OF_NEIGHBORS_WITHIN_RADIUS = getNumberOfNeighborsWithinRadius(2);
        Location [] neighbors = new Location[NUMBER_OF_NEIGHBORS_WITHIN_RADIUS];
        int index = 0;
        //(x, y-2)
        if(y >= 2) {
            neighbors[index++] = LocationFactory.createLocation(x, y - 2);
        }
        //(x-1, y-1)
        if(x >= 1 && y >=1) {
            neighbors[index++] = LocationFactory.createLocation(x-1, y-1);
        }
        //(x, y-1)
        if(y >= 1) {
            neighbors[index++] = LocationFactory.createLocation(x, y-1);
        }
        //(x+1, y-1)
        if(x <= WIDTH - 2 && y >= 1) {
            neighbors[index++] = LocationFactory.createLocation(x+1, y-1);
        }
        //(x-2, y)
        if(x >= 2) {
            neighbors[index++] = LocationFactory.createLocation(x-2, y);
        }
        //(x-1, y)
        if(x >= 1) {
            neighbors[index++] = LocationFactory.createLocation(x-1, y);
        }
        //(x+1, y)
        if(x <= WIDTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(x+1, y);
        }
        //(x+2, y)
        if(x <= WIDTH - 3) {
            neighbors[index++] = LocationFactory.createLocation(x+2, y);
        }
        //(x-1, y+1)
        if(x >= 1 && y <= LENGTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(x-1, y+1);
        }
        //(x, y+1)
        if(y <= LENGTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(x, y+1);
        }
        //(x+1, y+1)
        if(x <= WIDTH - 2 && y <= LENGTH - 2) {
            neighbors[index++] = LocationFactory.createLocation(x+1, y+1);
        }
        //(x, y+2)
        if(y <= LENGTH - 3) {
            neighbors[index++] = LocationFactory.createLocation(x, y+2);
        }
        return Arrays.copyOf(neighbors, index);
    }
}