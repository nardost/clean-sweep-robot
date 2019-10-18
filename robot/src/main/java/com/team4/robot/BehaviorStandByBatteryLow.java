package com.team4.robot;
package com.team4.common;

public class BehaviorStandByBatteryLow implements Behavior {
	
	@Override
    public void work() {
		// Whatever
	}
	
	@Override
	State GetState() {
		return State.LOW_BATTERY;
	}
}
