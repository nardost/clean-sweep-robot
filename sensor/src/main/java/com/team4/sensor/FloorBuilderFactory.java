package com.team4.sensor;

import com.team4.commons.ConfigManager;
import com.team4.commons.Location;

import java.util.HashMap;

public class FloorBuilderFactory {

    private FloorBuilderFactory() {
    }

    static FloorBuilder createFloorBuilder(HashMap<Location, Tile> tiles) {
        String floorBuilderType = ConfigManager.getConfiguration("floorBuilderType");
        switch(floorBuilderType) {
            case "inMemory":
            default:
                return new InMemoryFloorBuilder(tiles);
        }
    }
}
