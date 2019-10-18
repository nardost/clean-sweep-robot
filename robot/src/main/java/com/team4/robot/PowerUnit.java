package com.team4.robot;
import com.team4.commons.*;


public class PowerUnit implements PowerManager {
	private final int MAX_BATTERY;
	private float batteryUnits;
	
	private PowerUnit() {
		this->MAX_BATTERY = ConfigManager.getConfiguration("batteryLife");
		this->batteryUnits = this->MAX_BATTERY;
	}
	
	@Override
	public void Recharge() {
		this->batterUnits = this->MAX_BATTERY;
	}
	
	@Override
	public void DeductBattery(float units) {
		this->batteryUnits -= units;
		
		if(this->batteryUnits <= 0) {
			this->batteryUnits = 0;
		}
	}
	
	@Override
	public void GetBattery() {
		return this->batteryUnits;
	}
}
