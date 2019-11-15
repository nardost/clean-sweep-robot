package com.team4.robot;

import static com.team4.commons.Direction.EAST;
import static com.team4.commons.Direction.NORTH;
import static com.team4.commons.Direction.SOUTH;
import static com.team4.commons.Direction.WEST;

import java.util.ArrayList;
import java.util.Arrays;

import com.team4.commons.Direction;
import com.team4.commons.Location;
import com.team4.commons.LocationFactory;

class NavigatorOmega implements Navigator {
	
	@Override
	public Direction traverseFloor(Direction [] directions) {

        int x = RobotCleanSweep.getInstance().getLocation().getX();
        int y = RobotCleanSweep.getInstance().getLocation().getY();

        ArrayList<Direction> dirList = new ArrayList<>(Arrays.asList(directions));
        if(dirList.contains(SOUTH) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x,y+1)))) {
        	
        	if(!(dirList.contains(WEST))) {
        		return SOUTH;
        	} else {
        		//if west visited
        		if((RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x-1, y)))) {
        			return SOUTH;
        		}
        		//west open and not visited
        		if(dirList.contains(EAST) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x+1, y)))) {
        			return WEST;
        		}
        	}
        }

        if(dirList.contains(EAST) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x+1, y)))) {
        	return EAST;
        }
        
        if(dirList.contains(NORTH) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x,y-1)))) {
        	return NORTH;
        }

        if(dirList.contains(WEST)) {
        	if (!(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x-1, y)))) {
        		return WEST;
        	}
        }
        //A* to go to last unvisited cell
        if(!RobotCleanSweep.getInstance().visitedAll()) {
        	Location current = RobotCleanSweep.getInstance().getLocation();
        	Location goal = RobotCleanSweep.getInstance().lastUnvisited();
        	AStar aStar = new AStar(RobotCleanSweep.getInstance().getGraph(), current, goal,1);
        	if(aStar.search() != null && !aStar.search().empty()) {
        	
        		return aStar.search().pop();
			}
        }
        return null;
	}
}
