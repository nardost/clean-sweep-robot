package com.team4.robot;

import com.team4.commons.Location;

class Heuristics {
	
	private Location goal;
	public Heuristics(Location goal) {
		this.goal = goal;
	}
	
	Location getGoal() {
		return this.goal;
	}
	
	 int manhattan(Node node) {
		 int val = Math.abs((node.getLocation().getX() - getGoal().getX())) + Math.abs(node.getLocation().getY() - getGoal().getY());
		 return val;
	 }
}
