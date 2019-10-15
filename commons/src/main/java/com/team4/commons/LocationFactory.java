package com.team4.commons;

import java.util.HashMap;

/**
 * This makes sure there is only one location object per pair of (x, y) coordinates.
 */
public class LocationFactory {

    private static HashMap<String, Location> existingLocations = new HashMap<>();

    private LocationFactory() {
    }

    private static HashMap<String, Location> getExistingLocations() {
        return existingLocations;
    }

    public static Location createLocation(int x, int y) {
        if(x < 0 || y < 0) {
            throw new RobotException("Negative coordinates not allowed in location creation.");
        }
        String key = Utilities.tupleToString(x, y);
        if(!getExistingLocations().containsKey(key)) {
            Location newLocation = new Location(x, y);
            getExistingLocations().put(key, newLocation);
        }
        return getExistingLocations().get(key);
    }
}
