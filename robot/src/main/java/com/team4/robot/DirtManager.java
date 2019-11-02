package com.team4.robot;

import com.team4.commons.*;
import com.team4.sensor.*;

class DirtManager implements VacuumCleaner {
	
	private final int MAX_DIRT = Integer.parseInt(ConfigManager.getConfiguration("dirtCapacity"));
	
	private int dirtLevel;
	
	DirtManager() {
		dirtLevel = 0;
	}
	
	@Override
	public void clean() {
		// before cleaning, check if tank is full
		if (dirtLevel >= MAX_DIRT) {
			RobotCleanSweep.getInstance().setState(State.FULL_TANK);
			emptyMeIndicator();
		}
		else {
			// clean tile at current location
			SensorSimulator.getInstance().setTileDone(RobotCleanSweep.getInstance().getLocation());
			dirtLevel++;
		}
	}
	
	public int getDirtLevel() {
		return dirtLevel;
	}
	
	public void setDirtLevel(int dirt) {
		dirtLevel = dirt;
	}
	
	public void emptyMeIndicator() {
		System.out.println("DIRT CAPACITY REACHED! PLEASE EMPTY ME!");
	}
	
	public void emptyTank() {
		dirtLevel = 0;
		System.out.println("Dirt tank emptied.");
	}

}
