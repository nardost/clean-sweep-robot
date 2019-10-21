package com.team4.robot;

import com.team4.commons.Direction;
import com.team4.commons.LocationFactory;
import static com.team4.commons.Direction.*;

import java.util.ArrayList;
import java.util.Arrays;

public class NavigatorBeta implements Navigator {

	@Override
	public Direction traverseFloor(Direction [] directions) {

        int x = RobotCleanSweep.getInstance().getLocation().getX();
        int y = RobotCleanSweep.getInstance().getLocation().getY();
        
        //SOUTH --> EAST --> NORTH --> WEST
        //only if-statements because it is in order of priority
        //if one if-statement doesn't return, then we will check the next
        
        ArrayList<Direction> dirList = new ArrayList<>(Arrays.asList(directions));
        
        if(dirList.contains(SOUTH) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x,y+1)))) {
        	if(!(dirList.contains(WEST))){
        		return SOUTH;
        	}
        	if((dirList.contains(WEST)) && (RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x-1,y)))) {
        		return SOUTH;
        	}	
        }
        
        if(dirList.contains(EAST) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x+1,y)))) {
        	return EAST;
        }
        
        if(dirList.contains(NORTH) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x,y-1)))) {
        	return NORTH;
        }
        
        if(dirList.contains(WEST) && !(RobotCleanSweep.getInstance().visitedLocation(LocationFactory.createLocation(x-1,y)))) {
        	return WEST;
        }
        return null;
	}
}
