package com.team4.commons;

public enum FloorType {
    BARE,
    LOW_PILE,
    HIGH_PILE;

	public int getValue(){
		switch(this) {
			case BARE: return 1;
			case LOW_PILE: return 2;
			case HIGH_PILE: return 3;
			default: throw new RobotException("Invalid floor type state");
		}
	}
}
