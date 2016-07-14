package grids;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dataTypes.AssumptionValues;
import dataTypes.Coordinates;
import dataTypes.TableLine;
import TableBorders.ExtractPaths;
public class Intersections {
	
	private ArrayList<Coordinates> intersections = new ArrayList<Coordinates>();
	private ArrayList<TableLine> horizontalLines = new ArrayList<TableLine>();
	private ArrayList<TableLine> verticalLines = new ArrayList<TableLine>();	
	
	
	public ArrayList<Coordinates> getIntersections() {
		return intersections;
	}


	public ArrayList<TableLine> getHorizontalLines() {
		return horizontalLines;
	}


	public ArrayList<TableLine> getVerticalLines() {
		return verticalLines;
	}


	public void sortLines(ArrayList<TableLine> tableLines) {
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
    				   				
    				//increasing length of line for better intersection
    				if (tableLine.getFromCoord().getX() < tableLine.getToCoord().getX()) {
    					float start = tableLine.getFromCoord().getX() - AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					if (start < 0) start = 0;
    					tableLine.getFromCoord().setX(start);
    					
    					float end = tableLine.getToCoord().getX() + AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					tableLine.getToCoord().setX(end);
    				}
    				
    				else {
    					float start = tableLine.getToCoord().getX() - AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					if (start<0) start = 0;
    					tableLine.getToCoord().setX(start);
    					
    					float end = tableLine.getFromCoord().getX() + AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					tableLine.getFromCoord().setX(end);
    				}
    				
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
    				//increasing length of line for better intersection
    				if (tableLine.getFromCoord().getY() < tableLine.getToCoord().getY()) {
    					float start = tableLine.getFromCoord().getY() - AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					if (start < 0) start = 0;
    					tableLine.getFromCoord().setY(start);
    					
    					float end = tableLine.getToCoord().getY() + AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					tableLine.getToCoord().setY(end);
    				}
    				else {
    					float start = tableLine.getToCoord().getY() - AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					if (start<0) start = 0;
    					tableLine.getToCoord().setY(start);
    					
    					float end = tableLine.getFromCoord().getY() + AssumptionValues.increasingLengthOfLinesForBetterIntersection;
    					tableLine.getFromCoord().setY(end);
    				}
    				
    				verticalLines.add(tableLine);
    			}
    		}
    		
    	}
	}
	
	
	public ArrayList<Coordinates> findIntersections(ArrayList<TableLine> tableLines) {
		
		sortLines(tableLines);		
		for (TableLine horizontalLine : horizontalLines){
			for (TableLine verticalLine : verticalLines){
				if (Line2D.linesIntersect(horizontalLine.getFromCoord().getX(), horizontalLine.getFromCoord().getY(), horizontalLine.getToCoord().getX(), horizontalLine.getToCoord().getY(), verticalLine.getFromCoord().getX(), verticalLine.getFromCoord().getY(), verticalLine.getToCoord().getX(), verticalLine.getToCoord().getY())){
					Coordinates intersection = new Coordinates();
					intersection.setX(verticalLine.getFromCoord().getX());
					intersection.setY(horizontalLine.getFromCoord().getY());
					intersections.add(intersection);
				}
			}
		}
		
		return intersections;
		
	}
	
	
	public static void main(String[] args) throws IOException{
		ArrayList<TableLine> tableLines = new ArrayList<TableLine>(new ExtractPaths().extractLinesOnPage("E:\\Analytics Practice\\testing pdf\\empsit.pdf" , 5));
		ArrayList<Coordinates> intersects = new ArrayList<Coordinates>(new Intersections().findIntersections(tableLines));
		
//		for (Coordinates intersection : intersects){
//			System.out.println("(" + intersection.getX() + ", " + intersection.getY() + ")");
//		}
	}
}
