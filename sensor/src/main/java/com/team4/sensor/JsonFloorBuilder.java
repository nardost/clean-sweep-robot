package com.team4.sensor;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.team4.commons.*;

import static com.team4.commons.Direction.*;
import static com.team4.commons.FloorType.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

class JsonFloorBuilder implements FloorBuilder {

    private FloorPlan floorPlan;
    private HashMap<Location, Tile> tiles;

    JsonFloorBuilder(HashMap<Location, Tile> tiles) {
        if(tiles == null) {
            throw new RobotException("Null HashMap not allowed for tiles.");
        }
        setTiles(tiles);
        setFloorPlan(deserialize());
    }

    private HashMap<Location, Tile> getTiles() {
        return tiles;
    }

    void setTiles(HashMap<Location, Tile> tiles) {
        if(tiles == null) {
            throw new RobotException("Null tiles encountered.");
        }
        this.tiles = tiles;
    }

    private FloorPlan getFloorPlan() {
        return this.floorPlan;
    }

    private void setFloorPlan(FloorPlan floorPlan) {
        if(floorPlan == null) {
            throw new RobotException("Null floor plan encountered.");
        }
        this.floorPlan = floorPlan;
        Floor.WIDTH = floorPlan.getSouthEastCornerCoordinates().getX();
        Floor.LENGTH = floorPlan.getSouthEastCornerCoordinates().getY();
    }

    @Override
    public void buildFloor() {
        // build WIDTH x LENGTH tiles
        buildTiles(Floor.WIDTH, Floor.LENGTH);

        // build walls
        for(FloorPlan.Obstacle obstacle : getFloorPlan().getObstacles()) {
            buildWall(obstacle.getFrom(), obstacle.getTo());
            //STAIRS are treated exactly like walls.
        }

        // open doors
        for(FloorPlan.Passage passage : getFloorPlan().getPassages()) {
            Location a = passage.getFrom();
            Location b = passage.getTo();
            openDoor(a, b);
        }

        // set charging stations
        for(Location location : getFloorPlan().getChargingStations()) {
            setChargingStation(location);
        }

        // load floor types
        for(FloorPlan.Area area : getFloorPlan().getAreas()) {
            setFloorType(area);
        }
        Location location = LocationFactory.createLocation(0,9);
        Tile tile = getTiles().get(location);
    }

    private void buildTiles(int w, int l) {
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < l; j++) {
                Tile tile = null;
                Location location = null;
                location = LocationFactory.createLocation(i, j);
                tile = TileFactory.createTile(location);
                tile.setFloorType(BARE);
                tile.setClean(false);
                tile.setWestOpen(true);
                tile.setNorthOpen(true);
                tile.setSouthOpen(true);
                tile.setEastOpen(true);
                getTiles().put(location, tile);
            }
        }
    }

    private void buildWall(Location from, Location to) {
        // Only vertical or horizontal walls allowed.
        if(from.getX() != to.getX() && from.getY() != to.getY()) {
            throw new RobotException("Only vertical or horizontal walls allowed.");
        }
        // from and to have to be different.
        if(from.getX() == to.getX() && from.getY() == to.getY()) {
            throw new RobotException("The two end points of a wall cannot be the same.");
        }
        // Vertical wall
        if(from.getX() == to.getX()) {
            int x1 = from.getX();
            int y1 = Utilities.min(from.getY(), to.getY());
            int y2 = Utilities.max(from.getY(), to.getY());
            if(x1 == 0 || x1 == Floor.WIDTH) {
                //outer walls
                Direction directionToBlock = (x1 == 0) ? WEST : EAST;
                int x = (x1 == 0) ? x1 : x1 - 1;
                for(int i = y1; i < y2; i++) {
                    Location location = LocationFactory.createLocation(x, i);
                    Tile tile = TileFactory.createTile(location);
                    blockPassage(tile, directionToBlock);
                }
            } else {
                //inner walls
                int x = x1;
                for(int i = y1; i < y2; i++) {
                    Location west = LocationFactory.createLocation(x - 1, i);
                    Location east = LocationFactory.createLocation(x, i);
                    Tile westTile = TileFactory.createTile(west);
                    Tile eastTile = TileFactory.createTile(east);
                    blockPassage(westTile, EAST);
                    blockPassage(eastTile, WEST);
                }
            }
            return;
        }
        // Horizontal wall
        if(from.getY() == to.getY()) {
            int x1 = Utilities.min(from.getX(), to.getX());
            int x2 = Utilities.max(from.getX(), to.getX());
            int y1 = from.getY();
            if(y1 == 0 || y1 == Floor.LENGTH) {
                // outer walls
                Direction directionToBlock = (y1 == 0) ? NORTH : SOUTH;
                int y = (y1 == 0) ? y1 : y1 - 1;
                for(int i = x1; i < x2; i++) {
                    Location location = LocationFactory.createLocation(i, y);
                    Tile tile = TileFactory.createTile(location);
                    blockPassage(tile, directionToBlock);
                }
            } else {
                // inner walls
                int y = y1;
                for(int i = x1; i < x2; i++) {
                    Location north = LocationFactory.createLocation(i, y - 1);
                    Location south = LocationFactory.createLocation(i, y);
                    Tile northTile = TileFactory.createTile(north);
                    Tile southTile = TileFactory.createTile(south);
                    blockPassage(northTile, SOUTH);
                    blockPassage(southTile, NORTH);
                }
            }
            return;
        }
    }

    /**
     *
     * Open a passage between two tiles a & b
     * @param a
     * @param b
     */
    private void openDoor(Location a, Location b) {
        //tiles must be different
        if(a.getX() == b.getX() && a.getY() == b.getY()) {
            throw new RobotException("Tiles must be different");
        }
        //tiles must be on same row or on same column.
        if(a.getX() != b.getX() && a.getY() != b.getY()) {
            throw new RobotException("Tiles must be on the same row or on the same column.");
        }
        //tiles must be adjacent
        if( (a.getX() == b.getX() && Math.abs(a.getY() - b.getY()) > 1) || (a.getY() == b.getY() && Math.abs(a.getX() - b.getX()) > 1) ) {
            throw new RobotException("Tiles must be adjacent");
        }
        //tiles on the same row
        if(a.getY() == b.getY()) {
            int x1 = Utilities.min(a.getX(), b.getX());
            int x2 = Utilities.max(a.getX(), b.getX());
            int y = a.getY();
            Location west = LocationFactory.createLocation(x1, y);
            Location east = LocationFactory.createLocation(x2, y);
            Tile westTile = TileFactory.createTile(west);
            Tile eastTile = TileFactory.createTile(east);
            openPassage(westTile, EAST);
            openPassage(eastTile, WEST);
        }
        //tiles in same column
        if(a.getX() == b.getX()) {
            int x = a.getX();
            int y1 = Utilities.min(a.getY(), b.getY());
            int y2 = Utilities.max(a.getY(), b.getY());
            Location north = LocationFactory.createLocation(x, y1);
            Location south = LocationFactory.createLocation(x, y2);
            Tile northTile = TileFactory.createTile(north);
            Tile southTile = TileFactory.createTile(south);
            openPassage(northTile, SOUTH);
            openPassage(southTile, NORTH);
        }
    }

    /**
     * Marks a location as a charging station
     * @param location
     */
    private void setChargingStation(Location location) {
        if(location == null) {
            throw new RobotException("Null tile encountered.");
        }
        TileFactory.createTile(location).setChargingStation(true);
    }

    private void setFloorType(FloorPlan.Area area) {
        if(area == null) {
            throw new RobotException("Null area object encountered in JsonFloorBuilder.setFloorType() method.");
        }
        Location topLeft = area.getTopLeft();
        Location bottomRight = area.getBottomRight();
        FloorType floorType = toFloorType(area.getFloorType());
        int x1 = topLeft.getX();
        int y1 = topLeft.getY();
        int x2 = bottomRight.getX();
        int y2 = bottomRight.getY();
        //validate input locations here!!
        for(int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                TileFactory.createTile(LocationFactory.createLocation(i, j)).setFloorType(floorType);
            }
        }
    }

    private FloorType toFloorType(String floorTypeString) {
        switch (floorTypeString) {
            case "BARE":
                return BARE;
            case "LOW_PILE":
                return LOW_PILE;
            case "HIGH_PILE":
                return HIGH_PILE;
            default:
                throw new RobotException("Invalid floor type encountered.");
        }
    }

    /**
     * Helper method to close passage in tile toward specified direction
     * @param tile
     * @param direction
     */
    private void blockPassage(Tile tile, Direction direction) {
        if(tile == null) {
            throw new RobotException("Null tile encountered in JsonFloorBuilder.blockPassage() method.");
        }
        if(direction == null) {
            throw new RobotException("Null direction encountered in JsonFloorBuilder.blockPassage() method.");
        }
        switch(direction) {
            case NORTH:
                tile.setNorthOpen(false);
                return;
            case SOUTH:
                tile.setSouthOpen(false);
                return;
            case EAST:
                tile.setEastOpen(false);
                return;
            case WEST:
                tile.setWestOpen(false);
                return;
            default:
                throw new RobotException("Impossible direction.");
        }
    }

    /**
     * Helper method to open passage in tile toward specified direction
     * @param tile
     * @param direction
     */
    private void openPassage(Tile tile, Direction direction) {
        if(tile == null) {
            throw new RobotException("Null tile encountered in JsonFloorBuilder.openPassage() method.");
        }
        if(direction == null) {
            throw new RobotException("Null direction encountered in JsonFloorBuilder.openPassage() method.");
        }
        switch(direction) {
            case NORTH:
                tile.setNorthOpen(true);
                return;
            case SOUTH:
                tile.setSouthOpen(true);
                return;
            case EAST:
                tile.setEastOpen(true);
                return;
            case WEST:
                tile.setWestOpen(true);
                return;
            default:
                throw new RobotException("Impossible direction.");
        }
    }

    public FloorPlan deserialize() {
        String floorPlanJson = ConfigManager.getConfiguration("floorPlan");
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(floorPlanJson);
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            Gson gson = new Gson();
            return gson.fromJson(reader, FloorPlan.class);
        } catch (UnsupportedEncodingException uee) {
            throw new RobotException("Unsupported encoding exception encountered");
        }
    }
}
