package com.team4.robot;

import com.team4.commons.ConfigManager;

public class PowerUnit implements PowerManager {

	private int batteryLevel;
	
	private PowerUnit() {
		int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
		setBatteryLevel(maxBatteryLevel);
	}
	
	@Override
	public void recharge() {
		try {
			//simulate delay while charging.
			Thread.sleep(2000L);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		int maxBatteryLevel = Integer.parseInt(ConfigManager.getConfiguration("maxBatteryLevel"));
		setBatteryLevel(maxBatteryLevel);
	}

	@Override
	public int getBatteryLevel() {
		return batteryLevel;
	}

	@Override
	public void updateBatteryLevel(int units) {
		setBatteryLevel(getBatteryLevel() - units);
	}

	private void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
}
