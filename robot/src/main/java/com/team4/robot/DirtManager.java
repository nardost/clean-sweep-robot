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
	public void clean(double cost) {
		int newDirtLevel = getDirtLevel() + 1;
		if(newDirtLevel > MAX_DIRT) {
			RobotCleanSweep.getInstance().setState(State.FULL_TANK);
			waitUntilTankEmpty();
		} else {
			setDirtLevel(newDirtLevel);
			RobotCleanSweep.getInstance().getPowerManager().updateBatteryLevel(cost);
			SensorSimulator.getInstance().setTileDone(RobotCleanSweep.getInstance().getLocation());
		}
	}

	@Override
	public int getDirtLevel() {
		return this.dirtLevel;
	}

	private void setDirtLevel(int dirt) {
		if(dirt < 0) {
			throw new RobotException("Invalid dirt level: " + dirt);
		}
		this.dirtLevel = dirt;
	}

	private void waitUntilTankEmpty() {
		//press any key to continue is better.
		System.out.println();
		LogManager.print("...DIRT TANK FULL... (awaiting human intervention)...", RobotCleanSweep.getInstance().getZeroTime());
		System.out.println();
		Utilities.doTimeDelay(3);
		emptyTank();
	}

	private void emptyTank() {
		setDirtLevel(0);
		FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(RobotCleanSweep.getInstance().getLocation());
		clean(floorDao.floorType.getCost());
		System.out.println("------------------------------");
		System.out.println();
		LogManager.print("...DIRT TANK EMPTY... (human action completed)...", RobotCleanSweep.getInstance().getZeroTime());
		System.out.println();
		RobotCleanSweep.getInstance().setState(State.WORKING);
	}
}
