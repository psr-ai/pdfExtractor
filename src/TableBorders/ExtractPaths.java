package TableBorders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;


import dataTypes.TableLine;
import dataTypes.PDFMeta;

public class ExtractPaths
{
	private ArrayList<TableLine> horizontalLines = new ArrayList<TableLine>();
	private ArrayList<TableLine> verticalLines = new ArrayList<TableLine>();
	//pdf meta
	private PDFMeta pdfMeta = new PDFMeta();
	

	public PDFMeta getPdfMeta() {
		return pdfMeta;
	}

	public void setPdfMeta(PDFMeta pdfMeta) {
		this.pdfMeta = pdfMeta;
	}

	public ArrayList<TableLine> getHorizontalLines() {
		return horizontalLines;
	}

	public void setHorizontalLines(ArrayList<TableLine> horizontalLines) {
		this.horizontalLines = horizontalLines;
	}

	public ArrayList<TableLine> getVerticalLines() {
		return verticalLines;
	}

	public void setVerticalLines(ArrayList<TableLine> verticalLines) {
		this.verticalLines = verticalLines;
	}

	public ArrayList<TableLine> extractLinesOnPage(String filePath, int pageNumber) throws IOException
    {
        PDDocument document = PDDocument.load(filePath);
        List<?> allPages = document.getDocumentCatalog().getAllPages();
        int i = pageNumber;

        PrintPaths printPaths = new PrintPaths();

        PDPage page = (PDPage) allPages.get(i);
        
        PDStream contents = page.getContents();
                
        if (contents != null)
        {
            printPaths.processStream(page, page.findResources(), page.getContents().getStream());
        }
        
        ArrayList<TableLine> tableLines = new ArrayList<TableLine>(printPaths.getTableLines());        
       
        //fix the lines
        tableLines = new ArrayList<TableLine>(fixTableLines(tableLines));
        
        //rotate the lines
        
        if ((pdfMeta.getPageRotation() == 90 && pdfMeta.getReadingType().equals("Remove Potrait Characters"))){
        	rotatingOrigin();		
    		tableLines = new ArrayList<TableLine>();    		
    		for (TableLine horizontalLine : getHorizontalLines()) tableLines.add(horizontalLine);
    		for (TableLine verticalLine : getVerticalLines()) tableLines.add(verticalLine);
        }
               
//        for (TableLine tableLine : tableLines){
//        	if (tableLine.isHorizontalLine()){
//        		System.out.print("Horizontal Line : ");
//        	}
//        	else if (tableLine.isVerticalLine()){
//        		System.out.print("Vertical Line : ");
//        	}
//        	System.out.println("Start : (" + tableLine.getFromCoord().getX() + ", " + tableLine.getFromCoord().getY() + ")" + " End: (" + tableLine.getToCoord().getX() + ", " +tableLine.getToCoord().getY() + ")");
//        }
        document.close();
        return tableLines;
        
        
    }
	
	public ArrayList<TableLine> fixTableLines(ArrayList<TableLine> tableLines) throws IOException{
		
		for (TableLine tableLine : tableLines){
        	
        	if (Float.compare(tableLine.getFromCoord().getX(), tableLine.getToCoord().getX()) == 0) {
        		float lengthOfLine = Math.abs(tableLine.getToCoord().getY() - tableLine.getFromCoord().getY());
        		
        		if (lengthOfLine < tableLine.getScaledWidth()){
        			
        			tableLine.getFromCoord().setX(tableLine.getFromCoord().getX() - tableLine.getScaledWidth()/ (float) 2);
        			tableLine.getToCoord().setX(tableLine.getToCoord().getX() + tableLine.getScaledWidth()/ (float) 2);
        			tableLine.getToCoord().setY(tableLine.getFromCoord().getY());
        			tableLine.setHorizontalLine(true);
        			tableLine.setScaledLength(tableLine.getScaledWidth());
        			tableLine.setScaledWidth(lengthOfLine);        			
        		}
        		
        		else {
        			tableLine.setVerticalLine(true);
        			tableLine.setScaledLength(lengthOfLine);
        		}
        		
        		
        	}
        	
        	else if (Float.compare(tableLine.getFromCoord().getY(), tableLine.getToCoord().getY()) == 0) {
        		
        		float lengthOfLine = Math.abs(tableLine.getToCoord().getX() - tableLine.getFromCoord().getX());
        		
        		if (lengthOfLine < tableLine.getScaledWidth()) {
        			
        			tableLine.getFromCoord().setY(tableLine.getFromCoord().getY() - tableLine.getScaledWidth()/ (float) 2);
        			tableLine.getToCoord().setY(tableLine.getToCoord().getY() + tableLine.getScaledWidth()/ (float) 2);
        			tableLine.getToCoord().setX(tableLine.getFromCoord().getX());
        			tableLine.setVerticalLine(true);
        			tableLine.setScaledLength(tableLine.getScaledWidth());
        			tableLine.setScaledWidth(lengthOfLine);
        			
        		}
        		
        		else {
        			tableLine.setHorizontalLine(true);
        			tableLine.setScaledLength(lengthOfLine);
        		}
        		
        	}
        	
        }
		
		//removing similar lines which were used to increase thickness
		ArrayList<TableLine> horizontalLines = new ArrayList<TableLine>();
		ArrayList<TableLine> verticalLines = new ArrayList<TableLine>();
		for (TableLine tableLine : tableLines){		
			
			if (tableLine.isHorizontalLine()){
				if (tableLine.getFromCoord().getX() > tableLine.getToCoord().getX()){
					float temp = tableLine.getFromCoord().getX();
					tableLine.getFromCoord().setX(tableLine.getToCoord().getX());
					tableLine.getToCoord().setX(temp);
				}
				horizontalLines.add(tableLine);
			}
			
			else if (tableLine.isVerticalLine()){
				if (tableLine.getFromCoord().getY() > tableLine.getToCoord().getY()){
					float temp = tableLine.getFromCoord().getY();
					tableLine.getFromCoord().setY(tableLine.getToCoord().getY());
					tableLine.getToCoord().setY(temp);
				}
				verticalLines.add(tableLine);
			}			
		}
		
//		for (int t = 0; t < horizontalLines.size(); t++){
//			
//			for (int i = horizontalLines.size()-1; i>=0; i--) {
//				if (!horizontalLines.get(t).equals(horizontalLines.get(i))) {
//					
//					if (Math.abs(horizontalLines.get(t).getFromCoord().getY() - horizontalLines.get(i).getFromCoord().getY()) < AssumptionValues.distanceBetweenSimilarLines || Math.abs(horizontalLines.get(t).getFromCoord().getY() - horizontalLines.get(i).getFromCoord().getY()) < AssumptionValues.distanceBetweenSimilarLines) {
//						
//						if (horizontalLines.get(i).getFromCoord().getX() < horizontalLines.get(t).getFromCoord().getX()){
//							horizontalLines.get(t).getFromCoord().setX(horizontalLines.get(i).getFromCoord().getX());
//						}
//						
//						if (horizontalLines.get(i).getToCoord().getX() > horizontalLines.get(t).getToCoord().getX()){
//							horizontalLines.get(t).getToCoord().setX(horizontalLines.get(i).getToCoord().getX());
//						}
//						horizontalLines.remove(i);
//					}					
//					
//				}
//			}
//			
//		}
//		for (int t=0; t<verticalLines.size(); t++){
//			
//			for (int i = verticalLines.size()-1; i>=0; i--) {
//				if (!verticalLines.get(t).equals(verticalLines.get(i))) {
//					
//					if (Math.abs(verticalLines.get(t).getFromCoord().getX() - verticalLines.get(i).getFromCoord().getX()) < AssumptionValues.distanceBetweenSimilarLines || Math.abs(verticalLines.get(t).getFromCoord().getX() - verticalLines.get(i).getFromCoord().getX()) < AssumptionValues.distanceBetweenSimilarLines) {
//						if (verticalLines.get(i).getFromCoord().getY() < verticalLines.get(t).getFromCoord().getY()){
//							verticalLines.get(t).getFromCoord().setY(verticalLines.get(i).getFromCoord().getY());
//						}
//						if (verticalLines.get(i).getToCoord().getY() > verticalLines.get(t).getToCoord().getY()){
//							verticalLines.get(t).getToCoord().setY(verticalLines.get(i).getToCoord().getY());
//						}						
//						verticalLines.remove(i);
//					}					
//				}
//			}
//			
//		}
		setHorizontalLines(horizontalLines);
		setVerticalLines(verticalLines);	

		tableLines = new ArrayList<TableLine>();
		
		for (TableLine horizontalLine : getHorizontalLines()) tableLines.add(horizontalLine);
		for (TableLine verticalLine : getVerticalLines()) tableLines.add(verticalLine);
				
		return tableLines;
	}
	
	public void rotatingOrigin() throws IOException{		
		ArrayList<TableLine> newHorizontalLines = new ArrayList<TableLine>();
		for (TableLine verticalLine : verticalLines){
			TableLine newHorizontalLine = new TableLine();
			
			newHorizontalLine.getFromCoord().setY(verticalLine.getFromCoord().getX());
			newHorizontalLine.getToCoord().setY(verticalLine.getFromCoord().getX());
			newHorizontalLine.getFromCoord().setX(pdfMeta.getPageHeight() - verticalLine.getToCoord().getY());
			newHorizontalLine.getToCoord().setX(pdfMeta.getPageHeight() - verticalLine.getFromCoord().getY());
			newHorizontalLine.setScaledLength(verticalLine.getScaledLength());
			newHorizontalLine.setScaledWidth(verticalLine.getScaledWidth());
			newHorizontalLine.setHorizontalLine(true);
			newHorizontalLines.add(newHorizontalLine);
			
		}
		
		
		ArrayList<TableLine> newVerticalLines = new ArrayList<TableLine>();
		
		for (TableLine horizontalLine : horizontalLines){
			TableLine newVerticalLine = new TableLine();
			
			newVerticalLine.getFromCoord().setY(horizontalLine.getFromCoord().getX());
			newVerticalLine.getToCoord().setY(horizontalLine.getToCoord().getX());
			newVerticalLine.getFromCoord().setX(pdfMeta.getPageHeight() - horizontalLine.getFromCoord().getY());
			newVerticalLine.getToCoord().setX(pdfMeta.getPageHeight() - horizontalLine.getFromCoord().getY());
			newVerticalLine.setScaledLength(horizontalLine.getScaledLength());
			newVerticalLine.setScaledWidth(horizontalLine.getScaledWidth());
			newVerticalLine.setVerticalLine(true);
			newVerticalLines.add(newVerticalLine);
			
		}
		setHorizontalLines(newHorizontalLines);		
		setVerticalLines(newVerticalLines);
	}    
}