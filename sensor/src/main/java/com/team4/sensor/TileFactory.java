package com.team4.sensor;

import com.team4.commons.Location;

import java.util.HashMap;

/**
 * This makes sure there is only one Tile object per location (sort of).
 * NOTE: This is not a perfect flyweight pattern implementation.
 */
class TileFactory {

    private static HashMap<Location, Tile> existingTiles = new HashMap<>();

    private TileFactory() {
    }

    static Tile createTile(Location location) {
        if(!getExistingTiles().containsKey(location)) {
            Tile newTile = new Tile(location);
            getExistingTiles().put(location, newTile);
        }
        return getExistingTiles().get(location);
    }

    private static HashMap<Location, Tile> getExistingTiles() {
        return existingTiles;
    }
}
