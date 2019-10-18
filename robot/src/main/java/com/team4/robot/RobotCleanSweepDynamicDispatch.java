package com.team4.robot;

import com.team4.commons.*;

import static com.team4.commons.State.*;

public class RobotCleanSweepDynamicDispatch implements Robot {
	private Behavior statefulBehavior;
	
	private RobotCleanSweepDynamicDispatch(){
		this.statefulBehavior = new BehaviorOff();
	}
    
	void setState(State state) {
        if(state == null) {
            throw new RobotException("Null state is not allowed.");
        }
        
        switch(state) {
        case State.OFF:
        	this.statefulBehavior = new BehaviorOff();
        	break;
        case State.Working:
        	this.statefulBehavior = new BehaviorWorking();
        	break;
        case State.LOW_BATTERY:
        	this.statefulBehavior = new BehaviorStandByBatteryLow();
        	break;
        case State.FULL_TANK:
        	this.statefulBehavior = new BehaviorStandByFullTank();
        	break;
        }
        this.state = state;
    }
	
	
	@Override
    public void turnOn() {
        setState(STANDBY);
        //Robot waits for cleaning schedule.
        System.out.println("Waiting for scheduled cleaning time...");
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        //Robot begins work.
        work();
    }

    @Override
    public void turnOff() throws  RobotException {
        setState(OFF);
    }
	
	private void work() {
		this.statefulBehavior.work();
	}
}
