package com.team4.sensor;

import com.team4.commons.*;



import java.util.HashMap;

import static com.team4.commons.FloorType.*;

class InMemoryFloorBuilder implements FloorBuilder {

    private HashMap<Location, Tile> tiles;

    InMemoryFloorBuilder(HashMap<Location, Tile> tiles) {
        if(tiles == null) {
            throw new RobotException("Null HashMap not allowed for tiles.");
        }
        this.tiles = tiles;
    }

    private HashMap<Location, Tile> getTiles() {
        return tiles;
    }

    @Override
    public void buildFloor() {
        Floor.WIDTH = Integer.parseInt(ConfigManager.getConfiguration("floorWidth"));
        Floor.LENGTH = Integer.parseInt(ConfigManager.getConfiguration("floorLength"));
        final int W = Floor.WIDTH;
        final int L = Floor.LENGTH;
        buildCornerTiles(W, L);
        buildNorthWall(W, L);
        buildSouthWall(W, L);
        buildWestWall(W, L);
        buildEastWall(W, L);
        buildInteriorTiles(W, L);
        //vert wall
        buildWall(
                LocationFactory.createLocation(W - 3, 0),
                LocationFactory.createLocation(W - 3, L - 3),
                new Location [] { LocationFactory.createLocation(W - 3, L - 2) }, true); //changed from L-3
        //hor wall
        buildWall(
                LocationFactory.createLocation(W - 3, L - 2),
                LocationFactory.createLocation(W - 1, L - 2),
                new Location [] {}, true);
        
        //vert wall
        buildWall(
                LocationFactory.createLocation(W - 7, L-4),
                LocationFactory.createLocation(W - 7, L - 1),
                new Location [] {  }, false);
        //hor
        buildWall(
                LocationFactory.createLocation(0, L-4),
                LocationFactory.createLocation(W - 7, L - 4),
                new Location [] {  },false);
    }

    private void buildCornerTiles(int W, int L)  {
        Tile t = null;
        Location l = null;
        l = LocationFactory.createLocation(0,0);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(false);
        t.setWestOpen(false);
        t.setNorthOpen(false);
        t.setSouthOpen(true);
        t.setEastOpen(true);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(W - 1,0);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(false);
        t.setWestOpen(true);
        t.setNorthOpen(false);
        t.setSouthOpen(true);
        t.setEastOpen(false);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(0,L - 1);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(false);
        t.setWestOpen(false);
        t.setNorthOpen(true);
        t.setSouthOpen(false);
        t.setEastOpen(true);
        getTiles().put(l, t);
        l = LocationFactory.createLocation(W - 1,L - 1);
        t = TileFactory.createTile(l);
        t.setFloorType(BARE);
        t.setClean(false);
        t.setWestOpen(true);
        t.setNorthOpen(true);
        t.setSouthOpen(false);
        t.setEastOpen(false);
        getTiles().put(l, t);
    }

    private void buildNorthWall(int W, int L) {
        for(int i = 1; i <= W - 2; i++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(i,0);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(false);
            t.setWestOpen(true);
            t.setNorthOpen(false);
            t.setSouthOpen(true);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildSouthWall(int W, int L) {
        for(int i = 1; i <= W - 2; i++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(i,L - 1);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(false);
            t.setWestOpen(true);
            t.setNorthOpen(true);
            t.setSouthOpen(false);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildWestWall(int W, int L) {
        for(int j = 1; j <= L - 2; j++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(0, j);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(false);
            t.setWestOpen(false);
            t.setNorthOpen(true);
            t.setSouthOpen(true);
            t.setEastOpen(true);
            getTiles().put(l, t);
        }
    }

    private void buildEastWall(int W, int L) {
        for(int j = 1; j <= L - 2; j++) {
            Tile t = null;
            Location l = null;
            l = LocationFactory.createLocation(W - 1, j);
            t = TileFactory.createTile(l);
            t.setFloorType(BARE);
            t.setClean(false);
            t.setWestOpen(true);
            t.setNorthOpen(true);
            t.setSouthOpen(true);
            t.setEastOpen(false);
            getTiles().put(l, t);
        }
    }

    private void buildInteriorTiles(int W, int L) {
        for(int i = 1; i <= W - 2; i++) {
            for(int j = 1; j <= L - 2; j++) {
                Tile t = null;
                Location l = null;
                l = LocationFactory.createLocation(i, j);
                t = TileFactory.createTile(l);
                t.setFloorType(BARE);
                t.setClean(false);
                t.setWestOpen(true);
                t.setNorthOpen(true);
                t.setSouthOpen(true);
                t.setEastOpen(true);
                getTiles().put(l, t);
            }
        }
    }

    private void buildWall(Location from, Location to, Location [] doors, boolean val) {
        //validate inputs - line has to be horizontal of vertical.
        int fromX = from.getX();
        int fromY = from.getY();

        int toX = to.getX();
        int toY = to.getY();
        
        // code for door
        for(int i = 0; i < doors.length;i++) {
        	Location door  = doors[i];
        	Location outDoor = LocationFactory.createLocation(door.getX()-1, door.getY());
        	
        	Tile outDoorTile = TileFactory.createTile(outDoor);
        	outDoorTile.setEastOpen(true);
        }

        if((fromX == toX && fromY == toY) || (fromX != toX && fromY != toY)) {
            throw new RobotException("Wall inputs not valid.");
        }
        //Horizontal Wall
        if(fromY == toY) {
            int x1 = Utilities.min(fromX, toX);
            int x2 = Utilities.max(fromX, toX);
            int y = fromY;
            if(y == 0) {
                throw new RobotException("Outer wall already constructed.");
            }
            Location l1 = null;
            Location l2 = null;
            Tile t1 = null;
            Tile t2 = null;
            for(int i = x1; i <= x2; i++) {
            	//made a change to make the horizontal wall outer
            	if(val) {
                    l1 = LocationFactory.createLocation(i, y );
                    l2 = LocationFactory.createLocation(i, y + 1 );
            	}
            	else {
                    l1 = LocationFactory.createLocation(i, y -1 );
                    l2 = LocationFactory.createLocation(i, y  );
            	}

                t1 = TileFactory.createTile(l1);
                t2 = TileFactory.createTile(l2);
                t1.setSouthOpen(false);
                t2.setNorthOpen(false);
            }
        } else if(fromX == toX) {

            int y1 = Utilities.min(fromY, toY);
            int y2 = Utilities.max(fromY, toY);
            int x = fromX;
            if(x == 0) {
                throw new RobotException("Outer wall already constructed.");
            }
 
            Location l1 = null;
            Location l2 = null;
            Tile t1 = null;
            Tile t2 = null;
            for(int i = y1; i <= y2; i++) {
            	if (val) {
            		 l1 = LocationFactory.createLocation(x -1, i);
                     l2 = LocationFactory.createLocation(x, i);
            	}
            	else {
            		 l1 = LocationFactory.createLocation(x, i);
                     l2 = LocationFactory.createLocation(x+1, i);
            	}

                t1 = TileFactory.createTile(l1);
                t2 = TileFactory.createTile(l2);
                t1.setEastOpen(false);
                t2.setWestOpen(false);
            }
        }
    }
}
