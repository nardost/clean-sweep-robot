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
	 private int chooseComp;
	 private Node pathNode;
	 
	 public AStar(HashMap<Location, List<Location>> graph, Location location, Location goal, int comp) {
		 this.graph = graph;
		 this.initial = new Node(location);
		 this.goal = goal;
		 this.chooseComp = comp;
		 this.pathNode = null;
	 }
	 
	  class fComparator implements Comparator<Node> {
	 	Heuristics h = new Heuristics(getGoal());
	 	public int compare(Node a, Node b) {
			a.setF( h.manhattan(a) + a.getCost());
			b.setF((b.getCost() + h.manhattan(b)));
			return (a.getF()  - b.getF());
		 }
	 }
	  
	  
	  class gComparator implements Comparator<Node> {
	 	//Heuristics h = new Heuristics(getGoal());
	 	public int compare(Node a, Node b) {
	 		 
	 		if( a.getMaxFloorCost() < b.getMaxFloorCost() )return -1;
	 		if( a.getMaxFloorCost() > b.getMaxFloorCost() )return 1;
			return 0;
		 }
	 }
	 
	 Node getPathNode(){
		 return this.pathNode;
	 }
	 
	 void setPathNode(Node node) {
		 this.pathNode = node;
	 }
	 Stack<Direction> path(Node node){
		 Node node1 = node;
		 Stack<Direction> directions = new Stack<Direction>();
		 while(node1.getParent()!=null) {
			 //System.out.println(node1.getMaxFloorCost());
			 directions.add(node1.getDirection());
			 node1 = node1.getParent();
		 }
		 return directions;
	 }
	 
	 Location getGoal() {
		 return this.goal;
	 }
	 
	 Stack<Direction> search() {
		 PriorityQueue<Node> pQueue;
		 if(chooseComp == 1) {
			  pQueue = new PriorityQueue<Node>(new fComparator());
		 }
		 else if( chooseComp == 2) {
			  pQueue = new PriorityQueue<Node>(new gComparator());
		 }
		 else {
			  pQueue = null;
			
		 }
		 
		 
		 ArrayList<Location> expanded = new ArrayList<Location>();
		 Node node = initial;
		 pQueue.add(node);
		 
		 while(!pQueue.isEmpty()) {
			 node =  pQueue.poll();
			 
			 pQueue.remove(node);
			 expanded.add(node.getLocation());
			 
			 // if we are the goal location
			 if(node.getLocation().equals(getGoal())) {
				 setPathNode(node);
				 path(node);
				 return path(node);
			 }
			 
			 //generate children
			 List<Location> children = getGraph().get(node.getLocation());

			 if(children != null) {
				 
				 for(Location child : children) {
					 //System.out.println("hello");
					 
					 if(!expanded.contains(child)) {
						 
						 Node childNode = new Node(child);
						 childNode.setParent(node);
						 childNode.setCost(node.getCost()+1);
						 childNode.setDirection();
						double parentCost = childNode.getParent().getFloor().getCost();
						double myCost = childNode.getFloor().getCost();
						double cost = (parentCost + myCost)/2;
						childNode.setFloorCost(cost);
						childNode.setMaxFloorCost(childNode.getFloorCost() + childNode.getParent().getMaxFloorCost());
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
