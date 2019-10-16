package com.team4.sensor;

import com.team4.commons.Direction;
import static com.team4.commons.Direction.*;
import com.team4.commons.Location;
import com.team4.commons.RobotException;

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
        dao.openPassages = new Direction[4];
        int index = 0;
        if(tile.isNorthOpen()) {
            dao.openPassages[index++] = NORTH;
        }
        if(tile.isSouthOpen()) {
            dao.openPassages[index++] = SOUTH;
        }
        if(tile.isEastOpen()) {
            dao.openPassages[index++] = EAST;
        }
        if(tile.isWestOpen()) {
            dao.openPassages[index++] = WEST;
        }
        dao.chargingStations = new Location[12];
        return dao;
    }

    public static int getNumberOfTiles() {
        return getFloor().getTiles().size();
    }

    public static int [] getFloorDimension() {
        return new int[] { getFloor().WIDTH, getFloor().LENGTH };
    }
}