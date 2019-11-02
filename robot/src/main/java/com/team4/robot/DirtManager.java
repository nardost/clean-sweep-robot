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
		SensorSimulator.getInstance().setTileDone(RobotCleanSweep.getInstance().getLocation());
		setDirtLevel(getDirtLevel() + 1);
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
		try {
			System.out.println();
			for(int i = 1; i <= 5; i++){
				LogManager.print("...DIRT TANK FULL... (awaiting human intervention)...", RobotCleanSweep.getInstance().getZeroTime());
				Thread.sleep(1000L);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		emptyTank();
	}

	private void emptyTank() {
		setDirtLevel(0);
		System.out.println();
		LogManager.print("Dirt tank emptied...", RobotCleanSweep.getInstance().getZeroTime());
		System.out.println();
		RobotCleanSweep.getInstance().setState(State.WORKING);
	}
}
