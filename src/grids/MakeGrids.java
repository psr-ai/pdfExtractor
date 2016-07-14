package grids;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dataTypes.Coordinates;
import dataTypes.TableLine;

public class MakeGrids {
	
	ArrayList<TableLine> tableLines = new ArrayList<TableLine>();
	ArrayList<Coordinates> intersections = new ArrayList<Coordinates>();
	ArrayList<TableLine> horizontalLines = new ArrayList<TableLine>();
	ArrayList<TableLine> verticalLines = new ArrayList<TableLine>();
	ArrayList<ArrayList<Coordinates>> sortedCoordinates = new ArrayList<ArrayList<Coordinates>>();
	
	public void sortLines(){
		ArrayList<TableLine> tempTableLines = new ArrayList<TableLine>();
		for (TableLine tableLine : tableLines){
			tempTableLines.add(tableLine);
		}	
		
		ArrayList<TableLine> tempHorizontalLines = new ArrayList<TableLine>();
		ArrayList<TableLine> tempVerticalLines = new ArrayList<TableLine>();
		
		for (TableLine tableLine : tempTableLines){
			if (tableLine.isHorizontalLine()) tempHorizontalLines.add(tableLine);
			if (tableLine.isVerticalLine()) tempVerticalLines.add(tableLine);
		}
		
		ArrayList<Float> yCoords = new ArrayList<Float>();
		for (TableLine tableLine : tempHorizontalLines){
			yCoords.add(tableLine.getFromCoord().getY());
		}
		
		Set<Float> tempYCoords = new HashSet<Float>(yCoords);
    	ArrayList<Float> uniqueYCoords = new ArrayList<Float>(tempYCoords);
    	Collections.sort(uniqueYCoords);
    	
    	for (float uniqueY : uniqueYCoords){
    		
    		for (TableLine tableLine : tempTableLines){
    			if (Float.compare(tableLine.getFromCoord().getY(), uniqueY) == 0){    				    				
    				horizontalLines.add(tableLine);
    			}
    		}
    		
    	}
    	
    	ArrayList<Float> xCoords = new ArrayList<Float>();
		for (TableLine tableLine : tempVerticalLines){
			xCoords.add(tableLine.getFromCoord().getX());
		}
		
		Set<Float> tempXCoords = new HashSet<Float>(xCoords);
    	ArrayList<Float> uniqueXCoords = new ArrayList<Float>(tempXCoords);
    	Collections.sort(uniqueXCoords);
    	
    	
    	for (float uniqueX : uniqueXCoords){
    		
    		for (TableLine tableLine : tempTableLines){
    			if (Float.compare(tableLine.getFromCoord().getX(), uniqueX) == 0){
    				verticalLines.add(tableLine);
    			}
    		}
    		
    	}
    	
    	
    	//sorting the points
    	
    	ArrayList<Float> yCoordsIntersections = new ArrayList<Float>();
		for (Coordinates intersection : intersections){
			yCoordsIntersections.add(intersection.getY());
		}
		
		Set<Float> tempYCoordsIntersections = new HashSet<Float>(yCoordsIntersections);
    	ArrayList<Float> uniqueYCoordsIntersections = new ArrayList<Float>(tempYCoordsIntersections);
    	Collections.sort(uniqueYCoordsIntersections);
    	
    	ArrayList<ArrayList<Coordinates>> ySortedCoordinates = new ArrayList<ArrayList<Coordinates>>();
    	
    	for (float uniqueY : uniqueYCoordsIntersections){
    		ArrayList<Coordinates> oneLineCoordinates = new ArrayList<Coordinates>();
    		for (Coordinates intersection : intersections){
    			if (Float.compare(intersection.getY(), uniqueY) == 0){    				    				
    				oneLineCoordinates.add(intersection);
    			}
    		}
    		ySortedCoordinates.add(oneLineCoordinates);    		
    	}
    	
    	ArrayList<ArrayList<Coordinates>> sortedCoordinates = new ArrayList<ArrayList<Coordinates>>();
    	
    	for (ArrayList<Coordinates> coordinateLine : ySortedCoordinates){
    		ArrayList<Float> xCoordsIntersections = new ArrayList<Float>();
    		for (Coordinates coordinate : coordinateLine){
    			xCoordsIntersections.add(coordinate.getX());
    		}
    		
    		Set<Float> tempXCoordsIntersections = new HashSet<Float>(xCoordsIntersections);
        	ArrayList<Float> uniqueXCoordsIntersections = new ArrayList<Float>(tempXCoordsIntersections);
        	Collections.sort(uniqueXCoordsIntersections);
        	
        	ArrayList<Coordinates> xSortedCoordinateLine = new ArrayList<Coordinates>();
        	for (float uniqueX : uniqueXCoordsIntersections){
        		
        		for (Coordinates intersection : intersections){
        			if (Float.compare(intersection.getX(), uniqueX) == 0){
        				xSortedCoordinateLine.add(intersection);
        			}
        		}
        		
        	}
        	
        	sortedCoordinates.add(xSortedCoordinateLine);
        	
    	}
    	
		this.sortedCoordinates = new ArrayList<ArrayList<Coordinates>>(sortedCoordinates);
	}
	
	public void makeGrids(){
		
		for (ArrayList<Coordinates> intersectionPointsLine : sortedCoordinates){
			
			for (int i=0 ; i<intersectionPointsLine.size()-1 ; i++){
					
				for (TableLine horizontalLine : horizontalLines){
					
					if (Float.compare(horizontalLine.getFromCoord().getY(), intersectionPointsLine.get(i).getY()) == 0){
						
						int count = i+1;
						boolean verticalLineFound = false;
						while(!verticalLineFound && count != intersectionPointsLine.size()){
							if (horizontalLine.getToCoord().getX() > intersectionPointsLine.get(count).getX()) {
								
								for (TableLine verticalLine : verticalLines){
									
									if (Float.compare(verticalLine.getFromCoord().getX(), intersectionPointsLine.get(count).getX()) == 0){
										verticalLineFound = true;
									}
								}
								
								
							}
							else break;
						}
						
					}
					
				}
				
				
				
			}
			
		}
		
	}
	

}
