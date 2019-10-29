package com.team4.robot;

import java.util.ArrayList;
import java.util.HashMap;

import com.team4.commons.Direction;
import com.team4.commons.Location;

public class BestPath {
	
	 private HashMap<String, Location> graph;
	 private Location location;
	 private Location goal;
	 
	 public BestPath(HashMap<String, Location> graph, Location location, Location goal) {
		 this.graph = graph;
		 this.location = location;
		 this.goal = goal;
	 }
	 
	 ArrayList<Direction> successor(){
		 
		 return null;
	 }
	 

}
