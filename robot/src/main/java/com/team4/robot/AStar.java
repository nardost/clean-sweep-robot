package com.team4.robot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import com.team4.commons.Direction;
import com.team4.commons.Location;

class AStar {
	
	 private HashMap<Location, List<Location>> graph = new HashMap<>();
	 private Node initial;
	 private Location goal;
	 
	 public AStar(HashMap<Location, List<Location>> graph, Location location, Location goal) {
		 this.graph = graph;
		 this.initial = new Node(location);
		 this.goal = goal;
	 }
	 
	  class fComparator implements Comparator<Node> {
	 	Heuristics h = new Heuristics(getGoal());
	 	public int compare(Node a, Node b) {
			a.setF( h.manhattan(a) + a.getCost());
			b.setF((b.getCost() + h.manhattan(b)));
			return (a.getF()  - b.getF());
		 }
	 }
	 
	 Stack<Direction> path(Node node){
		 Node node1 = node;
		 Stack<Direction> directions = new Stack<Direction>();
		 while(node1.getParent()!=null) {
			 directions.add(node1.getDirection());
			 node1 = node1.getParent();
		 }
		 return directions;
	 }
	 
	 Location getGoal() {
		 return this.goal;
	 }
	 
	 Stack<Direction> search() {
		 
		 PriorityQueue<Node> pQueue = new PriorityQueue<Node>(new fComparator());
		 ArrayList<Location> expanded = new ArrayList<Location>();
		 Node node = initial;
		 pQueue.add(node);
		 
		 while(!pQueue.isEmpty()) {
			 node =  pQueue.poll();
			 pQueue.remove(node);
			 expanded.add(node.getLocation());
			 
			 // if we are the goal location
			 if(node.getLocation().equals(getGoal())) {

				 path(node);
				 return path(node);
			 }
			 
			 //generate children
			 List<Location> children = getGraph().get(node.getLocation());
			 //System.out.println(" Parent: " + node.getLocation());
			// System.out.print("--> My Children: " );
			 if(children != null) {
				 for(Location child : children) {
					 if(!expanded.contains(child)) {
						 Node childNode = new Node(child);
						 childNode.setParent(node);
						 childNode.setCost(node.getCost()+1);
						 childNode.setDirection();
						 pQueue.add(childNode);
					 }
				 }
			 }
		 }
		return null;
	 }

	public HashMap<Location, List<Location>> getGraph() {
		return graph;
	}

	public void setGraph(HashMap<Location, List<Location>> graph) {
		this.graph = graph;
	}
}
