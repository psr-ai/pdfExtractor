package plugin.pdfextractor;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import dataTypes.Cell;
import dataTypes.Word;
import extractTable.ExtractPDFTable;
import dataTypes.TableRow;
import dataTypes.TextLine;

public class PrintTextLocations extends PDFTextStripper {
	TextPosition previousChar;
	boolean lineMatch;
	int trail = 0;
	float normalSpacing;
	//PrintWriter writer = new PrintWriter("C:\\Users\\prabhjot.rai\\Desktop\\output2.txt", "UTF-8");
	ArrayList<Double> spaceDifferences = new ArrayList<Double>();
	ArrayList<ArrayList<Double>> spaceDifferencesByLine = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<TextPosition>> characterList = new ArrayList<ArrayList<TextPosition>>();
	ArrayList<TextPosition> oneLineCharacterList = new ArrayList<TextPosition>();
	TableRow mainRow = new TableRow();
	boolean atleastOneCellAppendedPreviously = true;
	float searchTextLastCharacterYPosition,searchTextFirstCharacterYPosition = 0;
	TextLine currentLine = new TextLine();
	private String readingType = "";
	private float pageWidth;
	private float pageHeight;
	
    public float getPageWidth() {
		return pageWidth;
	}
	public void setPageWidth(float pageWidth) {
		this.pageWidth = pageWidth;
	}
	public float getPageHeight() {
		return pageHeight;
	}
	public void setPageHeight(float pageHeight) {
		this.pageHeight = pageHeight;
	}
	public String getReadingType() {
		return readingType;
	}
	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}
	
	
	//Main function to extract the pdf
    public ArrayList<ArrayList<TextPosition>> pdfExtractor(String filePath, int pageNumber, String readingType) throws Exception {
    	setReadingType(readingType);
        PDDocument document = null;
        File input = new File(filePath);
        document = PDDocument.load(input);
        if (document.isEncrypted()) {
            document.decrypt("");
        }        
        
        document.setAllSecurityToBeRemoved(true);
        @SuppressWarnings("unchecked")
		List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
        PDPage myPage = allPages.get(pageNumber);
        setPageWidth(myPage.getMediaBox().getWidth());
        setPageHeight(myPage.getMediaBox().getHeight());
        normalSpacing = this.getSpacingTolerance();
        PDStream contents = myPage.getContents();
        if (contents != null) {
        	//Function to extract the pdf characters and join the words (@Override processTextPosition(TextPosition text) function is called after this function)
            this.processStream(myPage, myPage.findResources(), myPage.getContents().getStream());
            if (!oneLineCharacterList.isEmpty())characterList.add(oneLineCharacterList);
        }	
    	
    	//sort characterList by x axis 
    	
		ArrayList<ArrayList<TextPosition>> sortedCharacterList = new ArrayList<ArrayList<TextPosition>>();
    	for (ArrayList<TextPosition> characterRow: characterList){
    		ArrayList<Float> xCoordinateCharacter = new ArrayList<Float>();
    		for (TextPosition character : characterRow){
    			xCoordinateCharacter.add(character.getX());
    		}
    		Set<Float> uniqueXsetCharacter = new HashSet<Float>(xCoordinateCharacter);
        	ArrayList<Float> uniqueXCharacter = new ArrayList<Float>(uniqueXsetCharacter);
        	Collections.sort(uniqueXCharacter);
        	
        	ArrayList<TextPosition> xSorted = new ArrayList<TextPosition>();
        	
        	for (float uniquex : uniqueXCharacter){
        		for (TextPosition character : characterRow){
        			if (character.getX() == uniquex) {
        				xSorted.add(character);
        			}
        		}
        	}
        	
        	sortedCharacterList.add(xSorted);        	
    	}
    	
    	//sort characterList by y axis
    	
    	ArrayList<ArrayList<TextPosition>> ySortedCharacterList = new ArrayList<ArrayList<TextPosition>>();
    	ArrayList<Float> yCoordinateCharacter = new ArrayList<Float>();
		for (ArrayList<TextPosition> characterList : sortedCharacterList){
			yCoordinateCharacter.add(characterList.get(0).getY());
		}
		Set<Float> uniqueYsetCharacter = new HashSet<Float>(yCoordinateCharacter);
    	ArrayList<Float> uniqueYCharacter = new ArrayList<Float>(uniqueYsetCharacter);
    	Collections.sort(uniqueYCharacter);
    	for (Float uniqueY : uniqueYCharacter){
    		for (ArrayList<TextPosition> characterList : sortedCharacterList){
        		if (Float.compare(characterList.get(0).getY(), uniqueY) == 0){
        			ySortedCharacterList.add(characterList);
        		}
        	}
    	}
    	document.close();
    	
    	return ySortedCharacterList;
            
    }
    
    public ArrayList<ArrayList<TextPosition>> pdfExtractor(String filePath, int pageNumber, int pageRotation, String readingType) throws Exception {
    	setReadingType(readingType);
        PDDocument document = null;
        File input = new File(filePath);
        document = PDDocument.load(input);
        if (document.isEncrypted()) {
            document.decrypt("");
        }        
        
        document.setAllSecurityToBeRemoved(true);
        @SuppressWarnings("unchecked")
		List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
        PDPage myPage = allPages.get(pageNumber);
        myPage.setRotation(pageRotation);
        setPageWidth(myPage.getMediaBox().getWidth());
        setPageHeight(myPage.getMediaBox().getHeight());
        normalSpacing = this.getSpacingTolerance();
        PDStream contents = myPage.getContents();
        if (contents != null) {
        	//Function to extract the pdf characters and join the words (@Override processTextPosition(TextPosition text) function is called after this function)
            this.processStream(myPage, myPage.findResources(), myPage.getContents().getStream());
            if (!oneLineCharacterList.isEmpty())characterList.add(oneLineCharacterList);
        }	
    	
    	//sort characterList by x axis 
    	
		ArrayList<ArrayList<TextPosition>> sortedCharacterList = new ArrayList<ArrayList<TextPosition>>();
    	for (ArrayList<TextPosition> characterRow: characterList){
    		ArrayList<Float> xCoordinateCharacter = new ArrayList<Float>();
    		for (TextPosition character : characterRow){
    			xCoordinateCharacter.add(character.getX());
    		}
    		Set<Float> uniqueXsetCharacter = new HashSet<Float>(xCoordinateCharacter);
        	ArrayList<Float> uniqueXCharacter = new ArrayList<Float>(uniqueXsetCharacter);
        	Collections.sort(uniqueXCharacter);
        	
        	ArrayList<TextPosition> xSorted = new ArrayList<TextPosition>();
        	
        	for (float uniquex : uniqueXCharacter){
        		for (TextPosition character : characterRow){
        			if (character.getX() == uniquex) {
        				xSorted.add(character);
        			}
        		}
        	}
        	
        	sortedCharacterList.add(xSorted);        	
    	}
    	
    	//sort characterList by y axis
    	
    	ArrayList<ArrayList<TextPosition>> ySortedCharacterList = new ArrayList<ArrayList<TextPosition>>();
    	ArrayList<Float> yCoordinateCharacter = new ArrayList<Float>();
		for (ArrayList<TextPosition> characterList : sortedCharacterList){
			yCoordinateCharacter.add(characterList.get(0).getY());
		}
		Set<Float> uniqueYsetCharacter = new HashSet<Float>(yCoordinateCharacter);
    	ArrayList<Float> uniqueYCharacter = new ArrayList<Float>(uniqueYsetCharacter);
    	Collections.sort(uniqueYCharacter);
    	for (Float uniqueY : uniqueYCharacter){
    		for (ArrayList<TextPosition> characterList : sortedCharacterList){
        		if (Float.compare(characterList.get(0).getY(), uniqueY) == 0){
        			ySortedCharacterList.add(characterList);
        		}
        	}
    	}
    	document.close();
    	
    	return ySortedCharacterList;
            
    }
       
    public boolean ifCellOverlaps(Cell cell1, Cell cell2){
    	Float startCell1 = cell1.getStart().getX();
    	Float endCell1 = cell1.getEnd().getX();
    	Float startCell2 = cell2.getStart().getX();
    	Float endCell2 = cell2.getEnd().getX();
    	
    	if (!(endCell1<=startCell2 || startCell1>=endCell2)){
    		return true;
    	}
    	
    	else return false;   	
    	
    }
    
	protected boolean ifOverlapsTextCharacter(TextPosition text1, TextPosition text2){
    	Float startY1 = text1.getYDirAdj();
    	Float startY2 = text2.getYDirAdj();
    	Float endY1 = text1.getYDirAdj() + text1.getHeight();
    	Float endY2 = text2.getYDirAdj() + text2.getHeight();
    	
    	if (!(endY1<=startY2 || startY1>=endY2)){
    		return true;
    	}
    	
    	else return false;
    }
    
    
    @Override
    protected void processTextPosition(TextPosition text) {
    	
    	if (readingType.equals("Remove Landscape Characters")){
    		if (text.getTextPos().getValue(0,0) > 0){
    	    	trail++;
    	       
    	        //System.out.println(trail + " " + text.getCharacter() + " Place: X:" + text.getXDirAdj() + " x:: " + text.getX()+ " y::  " + text.getY() + " Y:" + text.getYDirAdj() + " Width" + text.getWidth() + " WidthDirAdj" + text.getWidthDirAdj() + " Text position:" + text.getTextPos() + " Font Size:" + text.getFontSize() + " Width of Space size:" + text.getWidthOfSpace() + " Font size in pt:" + text.getFontSizeInPt() + " X Scale:" + text.getXScale() + " width of space: " + text.getWidthOfSpace() + " individual widths:" + text.getIndividualWidths() + "Diacritic:" + text.isDiacritic());
    	        
    	        lineMatch = matchCharLine(text);
    	        //if (trail==1) writer.print("('" + text.getCharacter() + "')");
//    	        else if (lineMatch && !text.getCharacter().equals(" ")){  
//    	        	double value = text.getXDirAdj()-lastWordXDistance-previousChar.getWidth();
//    	        	DecimalFormat df = new DecimalFormat("0.00");
//    	        	String formattedValue = df.format(value);
//    	        	writer.print("(" + formattedValue + ")");
//    	        	spaceDifferences.add(Double.parseDouble(formattedValue));
//    	        	writer.print("('" + text.getCharacter() + "')");
//    	        }
    	        
    	        if (!text.getCharacter().equals(" ")){
    	        	oneLineCharacterList.add(text);
    	        }
    		}
    	}
    	
    	else if (readingType.equals("Remove Potrait Characters")){
    		if (Float.compare(text.getTextPos().getValue(0, 0), 0) == 0){
            	trail++;
               
                //System.out.println(trail + " " + text.getCharacter() + " Place: X:" + text.getXDirAdj() + " x:: " + text.getX()+ " y::  " + text.getY() + " Y:" + text.getYDirAdj() + " Width" + text.getWidth() + " WidthDirAdj" + text.getWidthDirAdj() + " Text position:" + text.getTextPos() + " Font Size:" + text.getFontSize() + " Width of Space size:" + text.getWidthOfSpace() + " Font size in pt:" + text.getFontSizeInPt() + " X Scale:" + text.getXScale() + " width of space: " + text.getWidthOfSpace() + " individual widths:" + text.getIndividualWidths() + "Diacritic:" + text.isDiacritic());
                
                lineMatch = matchCharLine(text);
                //if (trail==1) writer.print("('" + text.getCharacter() + "')");
//                else if (lineMatch && !text.getCharacter().equals(" ")){  
//                	double value = text.getXDirAdj()-lastWordXDistance-previousChar.getWidth();
//                	DecimalFormat df = new DecimalFormat("0.00");
//                	String formattedValue = df.format(value);
//                	writer.print("(" + formattedValue + ")");
//                	spaceDifferences.add(Double.parseDouble(formattedValue));
//                	writer.print("('" + text.getCharacter() + "')");
//                }
                
                if (!text.getCharacter().equals(" ")){
                	oneLineCharacterList.add(text);
                }
    		}    		
    	}
    	
    	else {
        	trail++;
           
            //System.out.println(trail + " " + text.getCharacter() + " Place: X:" + text.getXDirAdj() + " x:: " + text.getX()+ " y::  " + text.getY() + " Y:" + text.getYDirAdj() + " Width" + text.getWidth() + " WidthDirAdj" + text.getWidthDirAdj() + " Text position:" + text.getTextPos() + " Font Size:" + text.getFontSize() + " Width of Space size:" + text.getWidthOfSpace() + " Font size in pt:" + text.getFontSizeInPt() + " X Scale:" + text.getXScale() + " width of space: " + text.getWidthOfSpace() + " individual widths:" + text.getIndividualWidths() + "Diacritic:" + text.isDiacritic());
//            System.out.println(text.getCharacter() + " " + text.getTextPos().getValue(0, 0));
            lineMatch = matchCharLine(text);
            //if (trail==1) writer.print("('" + text.getCharacter() + "')");
//            else if (lineMatch && !text.getCharacter().equals(" ")){  
//            	double value = text.getXDirAdj()-lastWordXDistance-previousChar.getWidth();
//            	DecimalFormat df = new DecimalFormat("0.00");
//            	String formattedValue = df.format(value);
//            	writer.print("(" + formattedValue + ")");
//            	spaceDifferences.add(Double.parseDouble(formattedValue));
//            	writer.print("('" + text.getCharacter() + "')");
//            }
            
            if (!text.getCharacter().equals(" ")){
            	oneLineCharacterList.add(text);
            }
    	}
    	
    	
    }
    
    
    
    
     protected boolean matchCharLine(TextPosition text) {
    	
//    	Double yVal = roundVal(Float.valueOf(text.getY()));
        if (text.getX() > currentLine.getPreviousX() && (Float.compare(text.getY(), currentLine.getFromY())==0 || ifTextPositionOverlapsWithLine(currentLine,text))) {  
//        	if (text.getY() < currentLine.getFromY()) currentLine.setFromY(text.getY());
//        	if (text.getY() + text.getHeight() > currentLine.getToY()) currentLine.setToY(text.getY() + text.getHeight());
        	if (text.getX() > currentLine.getPreviousX()) currentLine.setPreviousX(text.getX());
        	return true;
        }
        previousChar = text;
//        DecimalFormat df = new DecimalFormat("#.00");
//        String formattedValue = df.format(value);        
//        writer.println();
//        writer.println(formattedValue);
//        spaceDifferencesByLine.add(spaceDifferences);
//        Collections.sort(spaceDifferences);
//        for (Double elem : spaceDifferences){
//        	System.out.print(elem + " ");
//        }
//        System.out.println();
//        spaceDifferences.clear();
        if (!oneLineCharacterList.isEmpty()) characterList.add(oneLineCharacterList);
        oneLineCharacterList = new ArrayList<TextPosition>();
        
        currentLine  = new TextLine();
        currentLine.setFromY(text.getY());
        currentLine.setToY(text.getY() + text.getHeight());
        currentLine.setPreviousX(text.getX());
        return false;
    }

    private boolean ifTextPositionOverlapsWithLine(TextLine currentLine2, TextPosition text) {
		
    	if (text.getY()>currentLine2.getToY() + 1 || (text.getY() + text.getHeight()) + 1 < currentLine2.getFromY()) {
    		return false;
    	}
    	
		return true;
	}
	protected Double roundVal(Float yVal) {
        DecimalFormat rounded = new DecimalFormat("0.0'0'");
        Double yValDub = new Double(rounded.format(yVal));
        return yValDub;
    }
	public <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;

        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }
        
    
    public PrintTextLocations()
            throws IOException {
        super.setSortByPosition(true);
    }
    
    public ArrayList<String> pdfPageExtractor(String filePath, int pageNumber) throws Exception {
	    
	    ArrayList<String> textRows = new ArrayList<String>();
	    
	    ArrayList<ArrayList<TextPosition>> sortedCharacterList = new ArrayList<ArrayList<TextPosition>>(pdfExtractor(filePath, pageNumber , ""));
	    ArrayList<ArrayList<Word>> sortedWordRows = new ArrayList<ArrayList<Word>>(new ExtractPDFTable().makeWords(sortedCharacterList));
	    for (ArrayList<Word> row : sortedWordRows){	    	
	    	String oneRowText = "";
	    	for (Word word : row){
	    		oneRowText = oneRowText + " " + word.getWord();
	    	}
	    	textRows.add(oneRowText);
	    }
	    return textRows;
	    
	}
    
       
            
}
