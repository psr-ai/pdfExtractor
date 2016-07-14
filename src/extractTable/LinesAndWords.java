package extractTable;

import java.io.IOException;
import java.util.ArrayList;


import dataTypes.AssumptionValues;
import dataTypes.Cell;
import dataTypes.PDFMeta;
import dataTypes.Table;
import dataTypes.TableLine;
import dataTypes.TableRow;

public class LinesAndWords {
	private ArrayList<TableLine> horizontalLines = new ArrayList<TableLine>();
	private ArrayList<TableLine> verticalLines = new ArrayList<TableLine>();
	private ArrayList<TableRow> extraction = new ArrayList<TableRow>();
	private float lastHorizontalLineCoordinate;
	private float headerLineCoordinate;
	private float endOfHeader = 0;
	private PDFMeta pdfMeta;
		
	public PDFMeta getPdfMeta() {
		return pdfMeta;
	}

	public void setPdfMeta(PDFMeta pdfMeta) {
		this.pdfMeta = pdfMeta;
	}

	public float getEndOfHeader() {
		return endOfHeader;
	}

	public void setEndOfHeader(float endOfHeader) {
		this.endOfHeader = endOfHeader;
	}

	public float getHeaderLineCoordinate() {
		return headerLineCoordinate;
	}

	public void setHeaderLineCoordinate(float headerLineCoordinate) {
		this.headerLineCoordinate = headerLineCoordinate;
	}

	public float getLastHorizontalLineCoordinate() {
		return lastHorizontalLineCoordinate;
	}

	public void setLastHorizontalLineCoordinate(float lastHorizontalLineCoordinate) {
		this.lastHorizontalLineCoordinate = lastHorizontalLineCoordinate;
	}

	public ArrayList<TableRow> getExtraction() {
		return extraction;
	}

	public void setHorizontalLines(ArrayList<TableLine> horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	public void setVerticalLines(ArrayList<TableLine> verticalLines) {
		this.verticalLines = verticalLines;
	}

	public void setExtraction(ArrayList<TableRow> extraction) {
		this.extraction = extraction;
	}
	
	public boolean findHeaderLine(TableRow mainRow, ArrayList<TableRow> extraction, ArrayList<TableLine> horizontalLines1) throws IOException{
		setHorizontalLines(horizontalLines1);
		
		for (int t = 0; t < horizontalLines.size(); t++){
			
			for (int i = horizontalLines.size()-1; i>=0; i--) {
				if (!horizontalLines.get(t).equals(horizontalLines.get(i))) {
					
					if (t<horizontalLines.size() && i>=0 && Math.abs(horizontalLines.get(t).getFromCoord().getY() - horizontalLines.get(i).getFromCoord().getY()) < AssumptionValues.distanceBetweenSimilarLines) {
						
						if (horizontalLines.get(i).getFromCoord().getX() < horizontalLines.get(t).getFromCoord().getX()){
							horizontalLines.get(t).getFromCoord().setX(horizontalLines.get(i).getFromCoord().getX());
						}
						
						if (horizontalLines.get(i).getToCoord().getX() > horizontalLines.get(t).getToCoord().getX()){
							horizontalLines.get(t).getToCoord().setX(horizontalLines.get(i).getToCoord().getX());
						}
						horizontalLines.remove(i);
					}					
					
				}
			}
			
		}
		ArrayList<TableLine> headerLines = new ArrayList<TableLine>();
		boolean foundHeaderLine = false;
		for (TableLine horizontalLine : horizontalLines){
			
			if (horizontalLine.getFromCoord().getX() < mainRow.getEntries().get(0).getEnd().getX() && horizontalLine.getToCoord().getX() > mainRow.getEntries().get(mainRow.getEntries().size()-1).getStart().getX() && horizontalLine.getFromCoord().getY() > extraction.get(0).getEntries().get(0).getStart().getY()){
				headerLines.add(horizontalLine);				
				foundHeaderLine = true;
			}
		}
		
		if (!foundHeaderLine) return false;
		
		float min = headerLines.get(0).getFromCoord().getY();
		setHeaderLineCoordinate(min);
		for (TableLine headerLine : headerLines){
			if (headerLine.getFromCoord().getY() < min){
				min = headerLine.getFromCoord().getY();
				setHeaderLineCoordinate(min);
			}
		}		
		
		return true;
		
	}

	public boolean ifCellOverlapsWithVerticalLine(Cell cell, TableLine verticalLine) throws IOException{
		
		if (verticalLine.getFromCoord().getY() > verticalLine.getToCoord().getY()){
			float temp = verticalLine.getFromCoord().getY();
			verticalLine.getFromCoord().setY(verticalLine.getToCoord().getY());
			verticalLine.getToCoord().setY(temp);			
		}
		
		
		if (verticalLine.getFromCoord().getY() < cell.getEntries().get(0).getStart().getY() && verticalLine.getToCoord().getY() > (cell.getEntries().get(0).getEnd().getY() - (cell.getEntries().get(0).getEnd().getY() - cell.getEntries().get(0).getStart().getY())/2 )) {
			return true;
		}
		
		return false;
		
		
	}
	
	
	public boolean findFooterLine(int mainRowColumns , Table table, ArrayList<TableLine> horizontalLines1){
		setHorizontalLines(horizontalLines1);
		for (int t = 0; t < horizontalLines.size(); t++){
			
			for (int i = horizontalLines.size()-1; i>=0; i--) {
				if (!horizontalLines.get(t).equals(horizontalLines.get(i))) {
					
					if ( t<horizontalLines.size() && i >=0 && Math.abs(horizontalLines.get(t).getFromCoord().getY() - horizontalLines.get(i).getFromCoord().getY()) < AssumptionValues.distanceBetweenSimilarLines) {
						
						if (horizontalLines.get(i).getFromCoord().getX() < horizontalLines.get(t).getFromCoord().getX()){
							horizontalLines.get(t).getFromCoord().setX(horizontalLines.get(i).getFromCoord().getX());
						}
						
						if (horizontalLines.get(i).getToCoord().getX() > horizontalLines.get(t).getToCoord().getX()){
							horizontalLines.get(t).getToCoord().setX(horizontalLines.get(i).getToCoord().getX());
						}
						horizontalLines.remove(i);
					}					
					
				}
			}
			
		}
		TableRow lastRow = new TableRow();
		float yCoordinate = 0;
		for (int i=table.getRows().size()-1 ; i >=0 ; i--){
			if (table.getRows().get(i).getEntries().size() == mainRowColumns){
				yCoordinate = table.getRows().get(i).getEntries().get(0).getStart().getY();
				lastRow = table.getRows().get(i);
				break;
			}
		}
		boolean foundLastHorizontalLine = false;
		for (TableLine tableLine : horizontalLines){
			if (tableLine.getFromCoord().getY() >= yCoordinate && tableLine.getFromCoord().getX()<lastRow.getEntries().get(0).getEnd().getX() && tableLine.getToCoord().getX()>lastRow.getEntries().get(lastRow.getEntries().size()-1).getStart().getX()){
				yCoordinate = tableLine.getFromCoord().getY();
				setLastHorizontalLineCoordinate(yCoordinate);
				foundLastHorizontalLine = true;
			}
		}
		if (foundLastHorizontalLine) return true;
		else return false;
	}
	
	public void extendCellsUsingVerticalLines() throws IOException{
		
		fixVerticalLines();	
		
		int count = 0;
		for (TableRow row : extraction){
			
			//To skip the last line of header rows with assumption that they are already one to one mapped.
			if (count == endOfHeader){
				continue;
			}
			for (int i=0; i<row.getEntries().size(); i++){
				
							
				if (i == 0){
					ArrayList<TableLine> overlappingVerticalLinesToLeft = new ArrayList<TableLine>();
					for (TableLine verticalLine : verticalLines){
						
						if (verticalLine.getFromCoord().getX() < row.getEntries().get(i).getStart().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){
							
							overlappingVerticalLinesToLeft.add(verticalLine);
						}
					}
					
					
					
					if (overlappingVerticalLinesToLeft.size()>0){
						float maxX = 0;
						for (TableLine verticalLine : overlappingVerticalLinesToLeft){
							if (verticalLine.getFromCoord().getX() > maxX){
								maxX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getStart().setX(maxX + 1);						
					}
					
					ArrayList<TableLine> overlappingVerticalLinesToRight = new ArrayList<TableLine>();
					
					if (i+1 < row.getEntries().size()){
						
						for (TableLine verticalLine : verticalLines){
							if (Float.compare(verticalLine.getFromCoord().getX(), (float) 239.76)==0){
								//System.out.println();
							}
							//System.out.println(verticalLine.getFromCoord().getX() + " " + verticalLine.getToCoord().getX() + " " + verticalLine.getFromCoord().getY() + " " + verticalLine.getToCoord().getY());
							if (verticalLine.getFromCoord().getX() > row.getEntries().get(i).getEnd().getX() && verticalLine.getFromCoord().getX() < row.getEntries().get(i+1).getStart().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){								
								overlappingVerticalLinesToRight.add(verticalLine);
							}
						}
					}
					
					else {
						for (TableLine verticalLine : verticalLines){
							if (verticalLine.getFromCoord().getX() > row.getEntries().get(i).getEnd().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){								
								overlappingVerticalLinesToRight.add(verticalLine);
							}
						}
					}
					
					if (overlappingVerticalLinesToRight.size() > 0){
						float minX = overlappingVerticalLinesToRight.get(0).getFromCoord().getX();
						for (TableLine verticalLine : overlappingVerticalLinesToRight){
							if (verticalLine.getFromCoord().getX() < minX){
								minX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getEnd().setX(minX - 1);
					}
					
					if (overlappingVerticalLinesToLeft.size() == 0 && overlappingVerticalLinesToRight.size()!=0 && i==0){
						row.getEntries().get(i).getStart().setX(0);
					}
					
					else if (overlappingVerticalLinesToLeft.size()!=0 && overlappingVerticalLinesToRight.size()==0 && i==row.getEntries().size()-1){
						row.getEntries().get(i).getEnd().setX(pdfMeta.getPageHeight());
					}
					
					
				}
				
				else if (i == row.getEntries().size() - 1){
					ArrayList<TableLine> overlappingVerticalLinesToLeft = new ArrayList<TableLine>();
					for (TableLine verticalLine : verticalLines){
						if (verticalLine.getFromCoord().getX() < row.getEntries().get(i).getStart().getX() && verticalLine.getFromCoord().getX() > row.getEntries().get(i-1).getEnd().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){
							overlappingVerticalLinesToLeft.add(verticalLine);
						}
					}
					
					if (overlappingVerticalLinesToLeft.size()>0){
						float maxX = 0;
						for (TableLine verticalLine : overlappingVerticalLinesToLeft){
							if (verticalLine.getFromCoord().getX() > maxX){
								maxX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getStart().setX(maxX + 1);						
					}
					
					ArrayList<TableLine> overlappingVerticalLinesToRight = new ArrayList<TableLine>();
					
					for (TableLine verticalLine : verticalLines){
						if (verticalLine.getFromCoord().getX() > row.getEntries().get(i).getEnd().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){								
							overlappingVerticalLinesToRight.add(verticalLine);
						}
					}
										
					if (overlappingVerticalLinesToRight.size() > 0){
						float minX = overlappingVerticalLinesToRight.get(0).getFromCoord().getX();
						for (TableLine verticalLine : overlappingVerticalLinesToRight){
							if (verticalLine.getFromCoord().getX() < minX){
								minX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getEnd().setX(minX - 1);
					}
					if (overlappingVerticalLinesToLeft.size() == 0 && overlappingVerticalLinesToRight.size()!=0 && i==0){
						row.getEntries().get(i).getStart().setX(0);
					}
					
					else if (overlappingVerticalLinesToLeft.size()!=0 && overlappingVerticalLinesToRight.size()==0 && i==row.getEntries().size()-1){
						row.getEntries().get(i).getEnd().setX(pdfMeta.getPageHeight());
					}				
					
				}
				
				else {
					ArrayList<TableLine> overlappingVerticalLinesToLeft = new ArrayList<TableLine>();
					for (TableLine verticalLine : verticalLines){
						if (verticalLine.getFromCoord().getX() < row.getEntries().get(i).getStart().getX() && verticalLine.getFromCoord().getX() > row.getEntries().get(i-1).getEnd().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){
							overlappingVerticalLinesToLeft.add(verticalLine);
						}
					}
					
					if (overlappingVerticalLinesToLeft.size()>0){
						float maxX = 0;
						for (TableLine verticalLine : overlappingVerticalLinesToLeft){
							if (verticalLine.getFromCoord().getX() > maxX){
								maxX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getStart().setX(maxX + 1);						
					}
					
					ArrayList<TableLine> overlappingVerticalLinesToRight = new ArrayList<TableLine>();
					
					
					for (TableLine verticalLine : verticalLines){
						
						if (verticalLine.getFromCoord().getX() > row.getEntries().get(i).getEnd().getX() && verticalLine.getFromCoord().getX() < row.getEntries().get(i+1).getStart().getX() && ifCellOverlapsWithVerticalLine(row.getEntries().get(i), verticalLine)){								
							overlappingVerticalLinesToRight.add(verticalLine);
						}
					}
										
					if (overlappingVerticalLinesToRight.size() > 0){
						float minX = overlappingVerticalLinesToRight.get(0).getFromCoord().getX();
						for (TableLine verticalLine : overlappingVerticalLinesToRight){
							if (verticalLine.getFromCoord().getX() < minX){
								minX = verticalLine.getFromCoord().getX();
							}
						}
						row.getEntries().get(i).getEnd().setX(minX - 1);
					}
					
					if (overlappingVerticalLinesToLeft.size() == 0 && overlappingVerticalLinesToRight.size()!=0 && i==0){
						row.getEntries().get(i).getStart().setX(0);
					}
					
					else if (overlappingVerticalLinesToLeft.size()!=0 && overlappingVerticalLinesToRight.size()==0 && i==row.getEntries().size()-1){
						row.getEntries().get(i).getEnd().setX(pdfMeta.getPageHeight());
					}
					
					
				}
				
				
				
				
			}
			
		}
		
		
	}
	
	public ArrayList<TableRow> extendCellsUsingVerticalLines(ArrayList<TableLine> verticalLines, ArrayList<TableRow> extraction) throws IOException{
		setVerticalLines(verticalLines);
		setExtraction(extraction);
		extendCellsUsingVerticalLines();
		
		return getExtraction();
	}
	
	public void fixVerticalLines(){
		
		
		
		for (int j=0; j< verticalLines.size(); j++){
			for (int i=0 ;i <verticalLines.size(); i++){
				if (i<verticalLines.size() && j<verticalLines.size() && !verticalLines.get(j).equals(verticalLines.get(i))){
					if (ifVerticalLinesOverlap(verticalLines.get(j), verticalLines.get(i))){						
						
						verticalLines.get(j).getFromCoord().setY(AssumptionValues.min(verticalLines.get(j).getFromCoord().getY(), verticalLines.get(i).getFromCoord().getY()));
						verticalLines.get(j).getToCoord().setY(AssumptionValues.max(verticalLines.get(j).getToCoord().getY(), verticalLines.get(i).getToCoord().getY()));
						verticalLines.remove(i);
						i--;
						
					}
					
				}
			}
		}		
		
		
	}
	
	public boolean ifVerticalLinesOverlap(TableLine line1, TableLine line2){
		float startY1 = line1.getFromCoord().getY();
		float startY2 = line2.getFromCoord().getY();
		float endY1 = line1.getToCoord().getY();
		float endY2 = line2.getToCoord().getY();
		
		float x1 = line1.getFromCoord().getX();
		float x2 = line2.getFromCoord().getX();
		
		
		
		if (!(endY1 + AssumptionValues.verticalDistanceBetweenSimilarLines < startY2 || endY2 + AssumptionValues.verticalDistanceBetweenSimilarLines < startY1) && Math.abs(x1-x2)<AssumptionValues.distanceBetweenSimilarLines){
			return true;
		}
		
		
		return false;
		
	}
}
