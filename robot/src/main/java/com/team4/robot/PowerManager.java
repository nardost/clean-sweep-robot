package com.team4.robot;
import com.team4.commons.State;

interface PowerManager {
	void Recharge();
	void DeductBattery(int units);
	int GetBattery();
}
