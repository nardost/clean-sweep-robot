package com.team4.sensor;

import com.team4.commons.ConfigManager;
import com.team4.commons.Location;
import java.util.HashMap;

class Floor {

    private HashMap<Location, Tile> tiles = new HashMap<>();

    static final int WIDTH = Integer.parseInt(ConfigManager.getConfiguration("floorWidth"));
    static final int LENGTH = Integer.parseInt(ConfigManager.getConfiguration("floorLength"));

    private static Floor theFloor = null;
    private Floor() {
        // pass a reference of the tiles hash map to the floor builder.
        // the floor builder uses that reference and builds tiles into it.
        FloorBuilderFactory.createFloorBuilder(getTiles()).buildFloor();
    }

    static Floor getInstance() {
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
}
