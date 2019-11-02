package com.team4.robot;

import java.util.Arrays;

class PrintPath {
	
	String [][] grid;
	int lastX;
	int lastY;
	int width;
	int length;
	public PrintPath(int x, int y) {
		this.width = x;
		this.length = y;
		this.grid = new String[length][width];
		for(int i =0; i<length; i++) {
			for(int j=0; j<width; j++){
				
				if(width>10 && length >10) {
					if(i>=10) {
						if(j<10) {
							grid[i][j] = "  " + j + "." + i + " ";
						}
						else {
							
						
						grid[i][j] = " " + j + "." + i + " ";
						}
					}
					else {
						if(j>=10) {
							grid[i][j] = "  " + j + "." + i + " ";
						}
						else {
							
						
						grid[i][j] = "   " + j + "." + i + " ";
						}
					}
					grid[0][0] = " START ";
					
				}
				else {
					grid[i][j] = " " + j + "." + i + " ";
					grid[0][0] = "START";
				}
			}
			}
		}
		
		
		//System.out.println();
		//System.out.println(Arrays.deepToString(grid).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
	
	
	public void VertWall(int xFr, int yFr, int yTo, boolean val) {
		String temp;
		for (int i = yFr; i <= yTo; i++) {
			if(width>10 && length >10) {
				// temp = grid[i][xFr];
				 //temp.trim();
				
			grid[i][xFr] = "   | ";
			}
			else {
				if(val) {
					grid[i][xFr] = "|"+grid[i][xFr].substring(1);
					//grid[i][xFr] = "|"+grid[i][xFr];
				}
				else {
					grid[i][xFr] = grid[i][xFr].substring(1)+"|";
					
				}
				
			}
		}

	}
	
	public void HorWall(int xFr, int xTo, int yFr, boolean val) {
		
//		for (int i = xFr; i <= xTo; i++) {
//			if(width>10 && length >10) {
//			grid[yFr][i] = "    __ ";
//		}
//			else {
//				//grid[yFr][i] = "  __ ";
//				
//			}
//		}
		int total;
		int sub;
		String space;
		if(width>10 && length >10) {
			 total = width * 7 + (width*2) - 2;
			 sub = ((xTo -xFr+1)*8)-2;
			 space = "       ";
		}
		else {
			
			 total = width * 5 + (width*2) - 2;
			 //sub = (xFr *5) + xFr;
			 sub = ((xTo -xFr+1)*6)-1;
			 space = "     ";
			 
			
		}
		if(val) {
			String verticSpace = "|" + new String(new char[total-sub]).replace("\0", " ") ;
			String verticWall =  new String(new char[(sub)]).replace("\0", "-") ;
			grid[yFr][xTo] = grid[yFr][xTo]+"|\n" + verticSpace + verticWall;
			
			
		}

		else {
			String verticSpace = " " + new String(new char[total-sub-2]).replace("\0", " ") ;
			String verticWall =  "|" + new String(new char[(sub)]).replace("\0", "-") ;
			grid[yFr-1][width-1] = grid[yFr-1][width-1]+"|\n" +verticWall+ (verticSpace)+"*" ;
			//(grid[yFr-1][width-1]+"|\n").replace("\n", "");
		}


	}
	
	public void add(String d, int x, int y) {
		String temp = grid[x][y].trim();
		if(temp.startsWith("|")){
			d = " "+ "|"+  d.substring(1, 4);
		}

		if(width>10 && length >10) {
	
		grid[x][y] = "  " + d;

		}
		else {
			grid[x][y] =   d;
		}
		
		this.lastX = x;
		this.lastY = y;
	}
	
	public void print() {
		
		int spaceInt; 
		if(width>10 && length >10) {
			 spaceInt = width * 7 + (width*2);
		}
		else {
			 spaceInt = width * 5 + (width*2);
		}
		
		
		
				
		String horWall = new String(new char[spaceInt-2]).replace("\0", "-");
		String verticWall = "|" + new String(new char[spaceInt-2]).replace("\0", " ") + "|";
		System.out.println(" " + horWall);
		
		
		System.out.println(Arrays.deepToString(grid).replace("], ", "|\n" +verticWall).replace("[[", "|").replace("]]", "|").replace(",", " ").replace("[", "\n|").replace("-|\n", "-").replace("*|\n", " ").replace(".", ","));
		System.out.println(" " + horWall);
		System.out.println();
		

	}
	public void end() {
		String end = "  END";
		String temp = grid[lastX][lastY].trim();
		
		if(temp.startsWith("|")){
			end = " "+ "|"+  end.trim();
		
		}
		if(width>10 && length >10) {

		grid[lastX][lastY]="  " + end;
		}
		else {
			grid[lastX][lastY]=end;
			
		}
	}
	
	public void door(int y, int x) {
		if(width>10 && length >10) {

			grid[x][y]="   }   ";
			}
			else {
				grid[x][y]= "}    ";
				
			}
		
	}

}
