package com.team4.sensor;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.team4.commons.*;
import static com.team4.commons.Direction.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static com.team4.commons.FloorType.BARE;

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
        return floorPlan;
    }

    private void setFloorPlan(FloorPlan floorPlan) {
        if(floorPlan == null) {
            throw new RobotException("Null floor plan encountered.");
        }
        this.floorPlan = floorPlan;
    }

    @Override
    public void buildFloor() {
        // get dimensions
        // build w x l tiles
        final int WIDTH = getFloorPlan().getSouthEastCornerCoordinates().getX();
        final int LENGTH = getFloorPlan().getSouthEastCornerCoordinates().getY();
        buildTiles(WIDTH, LENGTH);

        // build walls
        for(FloorPlan.Obstacle obstacle : getFloorPlan().getObstacles()) {
            buildWall(obstacle.getFrom(), obstacle.getTo());
            //STAIRS are treated exactly like walls.
        }

        // open doors
        for(FloorPlan.Passage passage : getFloorPlan().getPassages()) {
            //...
        }

        // set charging stations
        for(Location station : getFloorPlan().getChargingStations()) {
            //...
        }

        // load floor type
        for(FloorPlan.Area area : getFloorPlan().getAreas()) {
            //...
        }
    }

    private void buildTiles(int w, int l) {
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < l; j++) {
                Tile tile = null;
                Location location = null;
                location = LocationFactory.createLocation(i, j);
                tile = TileFactory.createTile(location);
                tile.setFloorType(BARE);
                tile.setClean(true);
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
            if(x1 == 0 || x1 == getFloorPlan().getSouthEastCornerCoordinates().getX()) {
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
            return;
        }
    }

    /**
     * Helper method to close passage in tile toward specified direction
     * @param tile
     * @param direction
     */
    private void blockPassage(Tile tile, Direction direction) {
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

    public FloorPlan deserialize() {
        String floorPlanJson = "floor-plan.json";
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
