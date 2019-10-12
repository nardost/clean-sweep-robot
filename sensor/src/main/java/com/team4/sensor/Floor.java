package com.team4.sensor;

import static com.team4.commons.FloorType.*;

import com.team4.commons.ConfigManager;
import com.team4.commons.Location;
import com.team4.commons.LocationFactory;
import com.team4.commons.RobotException;

import java.util.HashMap;

class Floor {

    final int WIDTH;
    final int LENGTH;
    private HashMap<Location, Tile> tiles = new HashMap<>();

    private static Floor theFloor = null;
    private Floor() throws RobotException {
        WIDTH = Integer.parseInt(ConfigManager.getConfiguration("floorWidth"));
        LENGTH = Integer.parseInt(ConfigManager.getConfiguration("floorLength"));
        buildFloorPlan();
    }

    static Floor getInstance() throws RobotException {
        if(theFloor == null) {
            synchronized (Floor.class) {
                if(theFloor == null) {
                    theFloor = new Floor();
                }
            }
        }
        return theFloor;
    }

    HashMap<Location, Tile> getTiles() {
        return tiles;
    }

    Tile getTile(Location location) {
        return getTiles().get(location);
    }

    private void buildFloorPlan() throws RobotException {
        final int W = WIDTH;
        final int L = LENGTH;
        buildCornerTiles(W, L);
        buildNorthWall(W, L);
        buildSouthWall(W, L);
        buildWestWall(W, L);
        buildEastWall(W, L);
    }

    private void buildCornerTiles(int W, int L) throws RobotException  {
        Tile t = null;
        Location l = null;
        l = LocationFactory.createLocation(0,0);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(true);
        t.setWestOpen(false);
        t.setNorthOpen(false);
        t.setSouthOpen(true);
        t.setEastOpen(true);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(W - 1,0);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(true);
        t.setWestOpen(true);
        t.setNorthOpen(false);
        t.setSouthOpen(true);
        t.setEastOpen(false);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(0,L - 1);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(true);
        t.setWestOpen(false);
        t.setNorthOpen(true);
        t.setSouthOpen(false);
        t.setEastOpen(true);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(W - 1,L - 1);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(true);
        t.setWestOpen(true);
        t.setNorthOpen(true);
        t.setSouthOpen(false);
        t.setEastOpen(false);
        getTiles().put(l, t);
    }

    private void buildNorthWall(int W, int L) throws RobotException {
        for(int i = 1; i <= W - 2; i++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(i,0);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(true);
            t.setWestOpen(true);
            t.setNorthOpen(false);
            t.setSouthOpen(true);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildSouthWall(int W, int L) throws RobotException {
        for(int i = 1; i <= W - 2; i++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(i,L - 1);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(true);
            t.setWestOpen(true);
            t.setNorthOpen(true);
            t.setSouthOpen(false);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildWestWall(int W, int L) throws RobotException {
        for(int j = 1; j <= L - 2; j++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(0, j);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(true);
            t.setWestOpen(true);
            t.setNorthOpen(true);
            t.setSouthOpen(false);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildEastWall(int W, int L) throws RobotException {
        for(int j = 1; j <= L - 2; j++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(W - 1, j);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(true);
            t.setWestOpen(true);
            t.setNorthOpen(true);
            t.setSouthOpen(true);
            t.setEastOpen(false);
            getTiles().put(l, t);
        }
    }

    private void buildInteriorTiles(int W, int L) throws RobotException {
        for(int i = 1; i <= W - 2; i++) {
            for(int j = 1; j <= L - 2; j++) {
                Tile t = null;
                Location l = null;
                l = LocationFactory.createLocation(i, j);
                t = TileFactory.createTile(l);
                t.setFloorType(BARE);
                t.setClean(true);
                t.setWestOpen(true);
                t.setNorthOpen(true);
                t.setSouthOpen(true);
                t.setEastOpen(true);
                getTiles().put(l, t);
            }
        }
    }
}
