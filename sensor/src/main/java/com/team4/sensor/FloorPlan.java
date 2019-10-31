package com.team4.sensor;


import com.team4.commons.Location;
import com.team4.commons.RobotException;

import java.util.List;

class FloorPlan {

    private Location southEastCornerCoordinates;
    private List<Obstacle> obstacles;
    private List<Passage> passages;
    private List<Location> chargingStations;
    private List<Area> areas;

    FloorPlan(Location southEastCornerCoordinates, List<Obstacle> obstacles, List<Passage> passages, List<Location> chargingStations, List<Area> areas) {
        this.southEastCornerCoordinates = southEastCornerCoordinates;
        this.obstacles = obstacles;
        this.passages = passages;
        this.chargingStations = chargingStations;
        this.areas = areas;
    }

    Location getSouthEastCornerCoordinates() {
        return southEastCornerCoordinates;
    }

    void setSouthEastCornerCoordinates(Location southEastCornerCoordinates) {
        if(southEastCornerCoordinates == null) {
            throw new RobotException("Invalid South East Corner Coordinates");
        }
        this.southEastCornerCoordinates = southEastCornerCoordinates;
    }

    List<Obstacle> getObstacles() {
        return obstacles;
    }

    List<Passage> getPassages() {
        return passages;
    }

    List<Location> getChargingStations() {
        return chargingStations;
    }

    List<Area> getAreas() {
        return areas;
    }

    class Obstacle {
        private Location from;
        private Location to;
        private String obstacleType;

        Obstacle(Location from, Location to, String obstacleType) {
            this.from = from;
            this.to = to;
            this.obstacleType = obstacleType;
        }

        Location getFrom() {
            return from;
        }

        Location getTo() {
            return to;
        }

        String getObstacleType() {
            return obstacleType;
        }
    }
    class Passage {
        private Location from;
        private Location to;

        Passage(Location from, Location to) {
            this.from = from;
            this.to = to;
        }

        Location getFrom() {
            return from;
        }

        Location getTo() {
            return to;
        }
    }

    class Area {
        private Location topLeft;
        private Location bottomRight;
        private String floorType;

        Area(Location topLeft, Location bottomRight, String floorType) {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
            this.floorType = floorType;
        }

        Location getTopLeft() {
            return topLeft;
        }

        Location getBottomRight() {
            return bottomRight;
        }

        String getFloorType() {
            return floorType;
        }
    }
}
