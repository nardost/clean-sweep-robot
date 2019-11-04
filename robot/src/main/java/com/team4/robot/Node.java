package com.team4.robot;

import com.team4.commons.Location;
import com.team4.sensor.FloorDao;
import com.team4.sensor.SensorSimulator;
import com.team4.commons.*;



class Node {
	private Location location;
	private Node parent;
	private int cost;
	private int maxCost;
	private Direction directionFromParent;
	private int f;
	private double floorCost;
	private double maxFloorCost;
	private FloorType floor;
	
	public Node(Location location) {
		setLocation(location);
		setParent(null);
		directionFromParent = null;
		setCost(0);
		setMaxCost(0);
		setF(0);
		setFloorCost(0);
		setMaxFloorCost(0);
		FloorDao floorDao = SensorSimulator.getInstance().getLocationInfo(location);
		setFloor(floorDao.floorType);
		
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

	public double getFloorCost() {
		return floorCost;
	}

	public void setFloorCost(double floorCost) {
		this.floorCost = floorCost;
	}
	
	public double getMaxFloorCost() {
		return maxFloorCost;
	}

	public void setMaxFloorCost(double floorCost) {
		this.maxFloorCost = floorCost;
	}

	public FloorType getFloor() {
		return floor;
	}

	public void setFloor(FloorType floor) {
		this.floor = floor;
	}
	
	
	
	
	

}
