package com.team4.robot;




import com.team4.commons.Direction;

import com.team4.commons.LocationFactory;
import com.team4.sensor.SensorSimulator;
import static com.team4.commons.Direction.*;
import static com.team4.commons.State.*;

public class NavigatorBeta implements Navigator{

	@Override
	public void traverseFloor() {
		// TODO Auto-generated method stub
		System.out.println("Hello");
		final int FLOOR_WIDTH = SensorSimulator.getInstance().getFloorDimension()[0];
        final int FLOOR_LENGTH = SensorSimulator.getInstance().getFloorDimension()[1];
		System.out.println("Width " + FLOOR_WIDTH + "Length: " + FLOOR_LENGTH);
		System.out.println(FLOOR_LENGTH);
		
		RobotCleanSweep.getInstance().setState(WORKING);
        int x = RobotCleanSweep.getInstance().getLocation().getX();
        int y = RobotCleanSweep.getInstance().getLocation().getY();
        //SOUTH --> WEST --> NORTH --> EAST
        while(RobotCleanSweep.getInstance().move(SOUTH)) {
        	
        }
		
	}
	
}


