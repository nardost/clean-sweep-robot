package com.team4.robot;
package com.team4.common;

public class BehaviorOff implements Behavior {
	
	@Override
    public void work() {
		// Do nothing
	}
	
	@Override
	State GetState() {
		return State.OFF;
	}
}
