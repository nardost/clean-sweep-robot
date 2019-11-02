package com.team4.commons;

public enum FloorType {
    BARE(1),
    LOW_PILE(2),
    HIGH_PILE(3);
	
	private final int val;
	
	FloorType(int val){
		this.val = val;
	}
	public int getValue(){
		return val;
	}
}
