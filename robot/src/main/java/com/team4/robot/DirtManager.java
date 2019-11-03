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
		SensorSimulator.getInstance().setTileDone(RobotCleanSweep.getInstance().getLocation());
		setDirtLevel(getDirtLevel() + (int) cost);
		RobotCleanSweep.getInstance().getPowerManager().updateBatteryLevel(cost);
	}

	private int getDirtLevel() {
		return this.dirtLevel;
	}

	private void setDirtLevel(int dirt) {
		if(dirt < 0 || dirt > MAX_DIRT) {
			throw new RobotException("Invalid dirt level: " + MAX_DIRT);
		}
		this.dirtLevel = dirt;
		if(this.dirtLevel == MAX_DIRT) {
			RobotCleanSweep.getInstance().setState(State.FULL_TANK);
			waitUntilTankEmpty();
		}
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
		System.out.println();
		LogManager.print("...DIRT TANK EMPTY... (human action completed)...", RobotCleanSweep.getInstance().getZeroTime());
		System.out.println();
		RobotCleanSweep.getInstance().setState(State.WORKING);
	}
}
