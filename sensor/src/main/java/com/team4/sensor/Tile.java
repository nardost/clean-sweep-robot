package com.team4.sensor;

import com.team4.commons.FloorType;
import com.team4.commons.Location;
import com.team4.commons.RobotException;

class Tile {
    private Location location;
    private FloorType floorType;
    private boolean isChargingStation;
    private boolean isNorthOpen;
    private boolean isSouthOpen;
    private boolean isEastOpen;
    private boolean isWestOpen;
    private boolean isClean;
    private int dirtUnits;

    private Tile() {
    }

    Tile(Location location) {
        this.location = location;
    }

    Location getLocation() {
        return location;
    }

    FloorType getFloorType() {
        return floorType;
    }

    boolean isChargingStation() {
        return isChargingStation;
    }

    boolean isNorthOpen() {
        return isNorthOpen;
    }

    boolean isSouthOpen() {
        return isSouthOpen;
    }

    boolean isEastOpen() {
        return isEastOpen;
    }

    boolean isWestOpen() {
        return isWestOpen;
    }

    boolean isClean() {
        return isClean;
    }

    int getDirtUnits() {
        return dirtUnits;
    }

    void setFloorType(FloorType floorType) {
        if(floorType == null) {
            throw new RobotException("Null floor type not allowed in Tiles.");
        }
        this.floorType = floorType;
    }

    void setChargingStation(boolean chargingStation) {
        isChargingStation = chargingStation;
    }

    void setNorthOpen(boolean northOpen) {
        isNorthOpen = northOpen;
    }

    void setSouthOpen(boolean southOpen) {
        isSouthOpen = southOpen;
    }

    void setEastOpen(boolean eastOpen) {
        isEastOpen = eastOpen;
    }

    void setWestOpen(boolean westOpen) {
        isWestOpen = westOpen;
    }

    void setClean(boolean clean) {
        isClean = clean;
    }

    void setDirtUnits(int dirtUnits) {
        if(0 <= dirtUnits && dirtUnits <= 10) {//(dirtUnits == 0 || dirtUnits == 1 || dirtUnits == 2 || dirtUnits == 3) {
            this.dirtUnits = dirtUnits;
            if(dirtUnits == 0) {
                setClean(true);
            }
        } else {
            throw new RobotException("Invalid dirt level " + dirtUnits);
        }
    }
}
