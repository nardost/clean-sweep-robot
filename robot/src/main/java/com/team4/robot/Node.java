package com.team4.robot;

import com.team4.commons.Direction.*;
import com.team4.commons.Location;
import com.team4.commons.*;



public class Node {
	private Location location;
	private Node parent;
	private int cost;
	private int maxCost;
	private Direction directionFromParent;
	private int f;
	
	public Node(Location location) {
		setLocation(location);
		setParent(null);
		directionFromParent = null;
		setCost(0);
		setMaxCost(0);
		setF(0);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(int maxCost) {
		this.maxCost = maxCost;
	}

	public Direction getDirection() {
		return directionFromParent;
	}

	public void setDirection() {
		//sameX
		int parentX = getParent().getLocation().getX();
		int parentY = getParent().getLocation().getY();	
		int meX = getLocation().getX();
		int meY= getLocation().getY();
		
		
		if(meX==parentX) {
			if(meY == parentY + 1) {
				this.directionFromParent = Direction.SOUTH;
				return;
			}
			if(meY == parentY -1) {
				this.directionFromParent = Direction.NORTH;
				return;
			}
		}
		
		if(meY==parentY) {
			if(meX == parentX + 1) {
				this.directionFromParent = Direction.EAST;
				return;
			}
			if(meX == parentX -1) {
				this.directionFromParent = Direction.WEST;
				return;
			}
		}
		
				
			
		
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}
	
	@Override
	public boolean equals(Object object ) {    
		
		if(!(object instanceof Node)) {
			return false;
		}
		Node node = (Node) object;
		
		return node.getLocation().equals(getLocation());
	}
	
	@Override
	public int hashCode() {			

		return this.getLocation().hashCode();
	}
	
	
	
	
	

}
