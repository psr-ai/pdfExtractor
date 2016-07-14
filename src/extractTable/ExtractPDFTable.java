package extractTable;

import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.IOException;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.TextPosition;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import TableBorders.ExtractPaths;
import dataTypes.AssumptionValues;
import dataTypes.Cell;
import dataTypes.Coordinates;
import dataTypes.ImproperArgsException;
import dataTypes.PDFMeta;
import dataTypes.SearchTextMeta;
import dataTypes.Table;
import dataTypes.TableRow;
import dataTypes.Word;
import plugin.pdfextractor.PrintTextLocations;

public class ExtractPDFTable {
	
	
//	static String[] headers = {"HOUSEHOLD DATA Table B. Employment status of the civilian population by sex and age, seasonally adjusted [Numbers in thousands]", "HOUSEHOLD DATA Table B. Employment status of the civilian population by sex and age, seasonally adjusted [Numbers in thousands]", "HOUSEHOLD DATA Summary table A. Household data, seasonally adjusted [Numbers in thousands]"};
//	static String[] footers = {"1 The population figures are not adjusted for seasonal variation.", "", "- Over-the-month changes are not displayed for not seasonally adjusted data."};
//	static String linkToAppendInNotepadFile="";
//	static String fileName = "empsit";
//	
//	static String headerText;
//	static String footerText;
//	static String filePath="C:\\Users\\prabhjot.rai\\Desktop\\Demo\\";	
//	static String excelFileName;
//	static String outputFilePath = filePath + fileName + "\\";
//	static PrintWriter writer;
	
	boolean atleastOneCellAppendedPreviously = true;
	TableRow mainRow = new TableRow();
	PDFMeta pdfMeta = new PDFMeta();
	float startYOfSearchText = 0.0f;
	float endYOfSearchText = 0.0f;
	ArrayList<String[]> pdfTable = new ArrayList<String[]>();
	
	private ArrayList<String> commentsOfExtraction;
	public ArrayList<String> getCommentsOfExtraction() {
		return commentsOfExtraction;
	}
	
	public ExtractPDFTable() {
		commentsOfExtraction = new ArrayList<String>();
	}
	
	
	
//    public static void main(String[] args) throws Exception{
//    	
//    	for (int i=0; i<headers.length; i++) {
//    		headerText = headers[i];
//    		footerText = footers[i];
//    		excelFileName = String.valueOf(i);
//    		
//    		writer = new PrintWriter(outputFilePath + excelFileName + ".txt", "UTF-8");
//        	writer.println("Header Text:" + headerText);
//        	writer.println("Footer Text:" + footerText);
//        	writer.println("File Name:" + fileName);
//        	writer.println("Link to pdf:" + linkToAppendInNotepadFile);
//        	writer.println("Errors:");
//    		
//    		new ExtractPDFTable().processTable(filePath + fileName + ".pdf", headerText , footerText);
//    	}
    		
    	//this code is not needed to be un-commentted
//    	new PrintTextLocations().pdfExtractor("E:\\Analytics Practice\\testing pdf\\h8.pdf", 0, 0, "Remove Potrait Characters");
//    }
    


	public ArrayList<SearchTextMeta> pagesContainingText(String filePath, String text) throws Exception{
    	PDDocument document= null;
    	ArrayList<SearchTextMeta> textsFound = new ArrayList<SearchTextMeta>();
    	File input = new File(filePath);
    	document = PDDocument.load(input);
    	@SuppressWarnings("unchecked")
		List<PDPage> pages = document.getDocumentCatalog().getAllPages();
    	int pageNumber = 0;
    	   	
    	for (PDPage page : pages){
    		if (page.getRotation()!=null) pdfMeta.setPageRotation(page.getRotation());
    		else pdfMeta.setPageRotation(0);
    		PrintTextLocations pdfExtraction = new PrintTextLocations();
    		ArrayList<ArrayList<TextPosition>> charList = new ArrayList<ArrayList<TextPosition>>(pdfExtraction.pdfExtractor(filePath, pageNumber, pdfMeta.getReadingType()));
    		FindTextMeta findText = new FindTextMeta();
    		if (findText.ifPageContainsText(charList, text)){
    			SearchTextMeta textFound = new SearchTextMeta();
    			textFound.setPageNumber(pageNumber);
    			textFound.setStartY(findText.getStartY());
    			textFound.setEndY(findText.getEndY());
    			textFound.setPageWidth(pdfExtraction.getPageWidth());
    			textFound.setPageHeight(pdfExtraction.getPageHeight());
    			textsFound.add(textFound);
    		}
    		pageNumber++;
    	}
    	return textsFound;
    }
    
    public ArrayList<SearchTextMeta> pagesContainingText(String filePath, String text, int pageRotation) throws Exception{
    	PDDocument document= null;
    	ArrayList<SearchTextMeta> textsFound = new ArrayList<SearchTextMeta>();
    	File input = new File(filePath);
    	document = PDDocument.load(input);
    	@SuppressWarnings("unchecked")
		List<PDPage> pages = document.getDocumentCatalog().getAllPages();
    	int pageNumber = 0;
    	   	
    	for (@SuppressWarnings("unused") PDPage page : pages){
    		PrintTextLocations pdfExtraction = new PrintTextLocations();
    		ArrayList<ArrayList<TextPosition>> charList = new ArrayList<ArrayList<TextPosition>>(pdfExtraction.pdfExtractor(filePath, pageNumber, pageRotation, pdfMeta.getReadingType()));
    		FindTextMeta findText = new FindTextMeta();
    		if (findText.ifPageContainsText(charList, text)){
    			SearchTextMeta textFound = new SearchTextMeta();
    			textFound.setPageNumber(pageNumber);
    			textFound.setStartY(findText.getStartY());
    			textFound.setEndY(findText.getEndY());
    			textFound.setPageWidth(pdfExtraction.getPageWidth());
    			textFound.setPageHeight(pdfExtraction.getPageHeight());
    			textsFound.add(textFound);
    		}
    		pageNumber++;
    	}
    	return textsFound;
    }
    
    
    
    public ArrayList<String[]> processTable(String filePath, String headerText, String footerText) throws Exception,ImproperArgsException{
    	ArrayList<SearchTextMeta> headerPages = new ArrayList<SearchTextMeta>();
    	ArrayList<SearchTextMeta> footerPages = new ArrayList<SearchTextMeta>();
    	if (!headerText.replace(" ", "").isEmpty()){
    		pdfMeta.setReadingType("Remove Landscape Characters");
    		headerPages = new ArrayList<SearchTextMeta>(pagesContainingText(filePath, headerText));
    		
    		if (!headerPages.isEmpty()){
    			pdfMeta.setHeaderTextOnPages(headerPages);
    		}
    		    		
    		else{
				pdfMeta.setReadingType("Remove Potrait Characters");
				headerPages = new ArrayList<SearchTextMeta>(pagesContainingText(filePath, headerText));
				
				if (!headerPages.isEmpty()){
					pdfMeta.setHeaderTextOnPages(headerPages);
				}
				
				else {
					pdfMeta.setReadingType("Remove Potrait Characters");
					int pageRotation = 0;
	    			for (int i=0 ; i<=360 ; i += 90){
	    				if (i!=pdfMeta.getPageRotation() && headerPages.isEmpty()) {
	    					headerPages = new ArrayList<SearchTextMeta>(pagesContainingText(filePath, headerText, i));
	    					pageRotation = i;
	    				}
	    			}
	    			if (!headerPages.isEmpty()){
	    				pdfMeta.setHeaderTextOnPages(headerPages);
	    				pdfMeta.setPageRotation(pageRotation);
	    			}
	    			else {
	        			pdfMeta.setReadingType("Remove Landscape Characters");
	        			pageRotation = 0;
	        			for (int i=0 ; i<=270 ; i += 90){
	        				if (i!=pdfMeta.getPageRotation() && headerPages.isEmpty()) {
	        					headerPages = new ArrayList<SearchTextMeta>(pagesContainingText(filePath, headerText, i));
	        					pageRotation = i;
	        				}
	        			}
	        			
	        			if (!headerPages.isEmpty()){
	        				pdfMeta.setHeaderTextOnPages(headerPages);
	        				pdfMeta.setPageRotation(pageRotation);
	        			}
	        			else {
	        				System.out.println("Cannot find the header text in any rotation! Try searching another text or try not to include any superscript or subscript in the header selection.");
//	        				writer.println("Cannot find the header text in any rotation! Try searching another text or try not to include any superscript or subscript in the header selection.");
	        				throw new ImproperArgsException("Cannot find the header text in any rotation! Try searching another text or try not to include any superscript or subscript in the header selection.");
	        				
	        			}
	        			
	        		}        			
				}
    		}
    		    		
    	}
    	
    	if (!footerText.replace(" ", "").isEmpty()){
    		if (!headerPages.isEmpty()) {
    			footerPages = new ArrayList<SearchTextMeta>(pagesContainingText(filePath, footerText, pdfMeta.getPageRotation()));
    			pdfMeta.setFooterTextOnPages(footerPages);
    			
    			if (footerPages.isEmpty()){
    				System.out.println("Can't find specified footer in the pdf. Processing table on page where header is located ...");
    				commentsOfExtraction.add("Can't find specified footer in the pdf. Processing table on page where header is located ...");
//    				writer.println("Can't find specified footer in the pdf. Processing table on page where header is located ...");
    			}
    		}
    		
    	}
    	    	    	
    	if (!headerPages.isEmpty() && !footerPages.isEmpty()) {
    		//finding first footer after header end
    		SearchTextMeta footerTextAfterHeader = new SearchTextMeta();
    		
    		for (int i=0; i<footerPages.size(); i++){
    			if (footerPages.get(i).getStartY() > headerPages.get(0).getEndY() && footerPages.get(i).getPageNumber()>=headerPages.get(0).getPageNumber()){
    				footerTextAfterHeader = new SearchTextMeta(footerPages.get(i));
    				break;
    			}
    		}
    		
    		
    		if (footerTextAfterHeader.getPageNumber()!=-1){
    			pdfMeta.setCurrentPageMeta(headerPages.get(0).getPageNumber());
        		for (int i=headerPages.get(0).getPageNumber() ; i<=footerTextAfterHeader.getPageNumber(); i++){
        			ArrayList<String[]> tempTable = new ArrayList<String[]>(processTable(filePath, i));
        			for (String[] row : tempTable){
        				pdfTable.add(row);
        			}
        			pdfMeta.setCurrentPageMeta(i+1);
        		}    		    			
    		}
    		
    		else {
    			System.out.println("Found footer but it lies before your input header line. Kindly refine your search.");
//    			writer.println("Found footer but it lies before your input header line. Kindly refine your search.");
    			throw new ImproperArgsException("Found footer but it lies before your input header line. Kindly refine your search.");
    			
    		}
    		
    		
    	}
    	
    	else if (!headerPages.isEmpty()){
    		pdfMeta.setCurrentPageMeta(headerPages.get(0).getPageNumber());
    		pdfTable = new ArrayList<String[]>(processTable(filePath, headerPages.get(0).getPageNumber()));
    	}
    	
    	if (!pdfTable.isEmpty()){
    		System.out.println("Table processed successfully.");
    	}
//    	writer.close();
//    	writeToExcel();   	
    	return pdfTable;
    }
    
//    private void writeToExcel() throws IOException {
//    	@SuppressWarnings("resource")
//		HSSFWorkbook workbook = new HSSFWorkbook();
//    	HSSFSheet sheet = workbook.createSheet("Sample sheet");    	
//    	
//    	for (int i=0; i<pdfTable.size(); i++){
//    		HSSFRow row1 = sheet.createRow(i);
//    		for (int j=0; j<pdfTable.get(i).length; j++){
//    			HSSFCell cell = row1.createCell(j);
//    			cell.setCellValue(pdfTable.get(i)[j]);
//    		}
//    	}
//    	
//    	FileOutputStream out = new FileOutputStream(new File(outputFilePath + excelFileName + ".xls"));
//        workbook.write(out);
//        out.close();
//		
//	}

	public ArrayList<String[]> processTable(String filePath, int pageNumber) throws Exception {
    	    	   	
    	ArrayList<ArrayList<TextPosition>> sortedCharacterList = new ArrayList<ArrayList<TextPosition>>(new PrintTextLocations().pdfExtractor(filePath, pageNumber, pdfMeta.getPageRotation(), pdfMeta.getReadingType())); 
    	
    	   	
    	if (Float.compare(pdfMeta.getTableStart(), 0.0f)!=0){
    		for (int i=sortedCharacterList.size()-1; i>=0; i--){
        		for (TextPosition text : sortedCharacterList.get(i)){
        			if (text.getY() <= pdfMeta.getTableStart()){
            			sortedCharacterList.remove(i);
            			break;
            		}
        		}
        		
        	}
    	}
    	
    	
    	if (Float.compare(pdfMeta.getTableEnd(), 0.0f)!=0){
    		for (int i=sortedCharacterList.size()-1; i>=0; i--){
        		for (TextPosition text : sortedCharacterList.get(i)){
        			if (text.getY() >= pdfMeta.getTableEnd()){
            			sortedCharacterList.remove(i);
            			break;
            		}
        		}
        		
        	}
    	}
    	
    	ArrayList<ArrayList<Word>> sortedWordRows = new ArrayList<ArrayList<Word>>(makeWords(sortedCharacterList));
    	
    	ArrayList<TableRow> extraction = new ArrayList<TableRow>();
    	
    	
    	
    	for (ArrayList<Word> wordRow : sortedWordRows) {
    		TableRow row = new TableRow();
    		
    		if (wordRow.size() == 1) {
    			Cell cell = new Cell();
				cell.addEntry(wordRow.get(0));
				row.addEntry(cell);
    		}
    		
    		else {
    			Cell currentCell = new Cell();
    			for (int i=1; i<wordRow.size() ; i++){
    				
        			            				            				
        			float left = wordRow.get(i).getStart().getX() - wordRow.get(i-1).getEnd().getX();
    				
        			if (i == wordRow.size()-1){
        				if (left < (wordRow.get(i).getWidthOfSpace()*AssumptionValues.wordsCollaboratingToCellFactor) || left < (wordRow.get(i-1).getWidthOfSpace()*AssumptionValues.wordsCollaboratingToCellFactor)){
        					currentCell.addEntry(wordRow.get(i-1));
        					currentCell.addEntry(wordRow.get(i));
        					row.addEntry(currentCell);              					
        					
        				}
        				
        				else {
        					currentCell.addEntry(wordRow.get(i-1));
        					row.addEntry(currentCell);
        					currentCell = new Cell();
        					currentCell.addEntry(wordRow.get(i));
        					row.addEntry(currentCell);                					
        				}
        			}
        			
        			else{
        				if (left < (wordRow.get(i).getWidthOfSpace()*AssumptionValues.wordsCollaboratingToCellFactor) || left < (wordRow.get(i-1).getWidthOfSpace()*AssumptionValues.wordsCollaboratingToCellFactor)){
        					currentCell.addEntry(wordRow.get(i-1));            					
        					
        				}
        				
        				else {
        					currentCell.addEntry(wordRow.get(i-1));
        					row.addEntry(currentCell);
        					currentCell = new Cell();
        					
        				}
            			
        			}
        			
        			
        		}
    			
    		}
    		
    		extraction.add(row);
    		
    	}
    	
//    	for (TableRow row: extraction) {
//    		System.out.print("Y: " + row.getEntries().get(0).getStart().getY());
//    		for (Cell cell : row.getEntries()){
//    			System.out.print(" (" + cell.getStart().getX() + ", " + cell.getEnd().getX() + ") " + cell.getCellContent());
//    		}
//    		System.out.println();
//    	}
    	
    	//removing headers based on previous page headers
    	if (pdfMeta.getHeaderLines()!=null){
    		int removeTillRowNumberBecauseOfPreviousPageHeaders = 0;
    		boolean foundEndOfHeader = false;
    		ArrayList<String> stringLines = new ArrayList<String>();
    		for (TableRow row : extraction){
    			String rowString = "";
    			for (Cell cell : row.getEntries()){
    				rowString = rowString + cell.getCellContent();
    			}
    			stringLines.add(rowString);
    		}
    		
    		for (int i=0 ; i<stringLines.size(); i++){
				
				for (int j=0; j<pdfMeta.getHeaderLines().size(); j++){
	    				    			
	    			if (j==pdfMeta.getHeaderLines().size()-1 && pdfMeta.getHeaderLines().get(j).equals(stringLines.get(i))){
	    				removeTillRowNumberBecauseOfPreviousPageHeaders = i;
	    				foundEndOfHeader = true;
	    				break;
	    			}
	    			
	    			if (pdfMeta.getHeaderLines().get(j).equals(stringLines.get(i))){
	    				i++;
	    				continue;
	    			}
	    			else break;						
    			
				}
				
				if (foundEndOfHeader){
					break;
				}
    		}
    		
    		if (removeTillRowNumberBecauseOfPreviousPageHeaders!=0){
    			for (int i=removeTillRowNumberBecauseOfPreviousPageHeaders ; i>=0; i--){
    				extraction.remove(i);
    			}
    		}
    		
    		
    		
    	}
    	
    	
    	mainRow = findMainRow(extraction);
    	
    	ExtractPaths lineExtractor = new ExtractPaths();
    	lineExtractor.setPdfMeta(pdfMeta);
    	lineExtractor.extractLinesOnPage(filePath , pageNumber);
    	LinesAndWords textAndLines = new LinesAndWords();
    	textAndLines.setPdfMeta(pdfMeta);
    	
    	//finding header line
    	
    	if (!lineExtractor.getHorizontalLines().isEmpty()){
    		boolean headerLineFound = textAndLines.findHeaderLine(mainRow, extraction, lineExtractor.getHorizontalLines());
        	
        	if (headerLineFound){
        		for (int i=extraction.size()-1 ; i>=0 ; i--){
        			if (extraction.get(i).getEntries().get(0).getStart().getY() < textAndLines.getHeaderLineCoordinate()){
        				pdfMeta.setEndOfHeaderLine(i);
        				break;
        			}
        		}
        	}
        	
        	else {
        		pdfMeta.setEndOfHeaderLine(0);
        	}    		
    	}
    	
    	else pdfMeta.setEndOfHeaderLine(0);
    	
    	
    	   	
    	textAndLines.setEndOfHeader(pdfMeta.getEndOfHeaderLine());
    	if (!lineExtractor.getVerticalLines().isEmpty()) extraction = new ArrayList<TableRow>(textAndLines.extendCellsUsingVerticalLines(lineExtractor.getVerticalLines(), extraction));   	    		
        	
    	
    	Table table = new Table();
    	table = createTable(extraction);
    	
    	//taking care of cells on left and right of main row
    	//boolean foundLeft = false;
    	for (TableRow row : table.getRows()){
    		
    		for (Cell cell : row.getEntries()){
    			
    			if (cell.getInit() == -1){
    				
    				// if the cell lies to the left hand side of main row start
    				if (cell.getEnd().getX() < mainRow.getEntries().get(0).getStart().getX()) {
    					cell.setInit(1);
    					cell.setColspan(1);
    					   					
    				}
    				
    				// if the cell lies on the right of last cell of main row
    				else if (cell.getStart().getX() > mainRow.getEntries().get(mainRow.getEntries().size()-1).getEnd().getX()){
    					cell.setInit(mainRow.getEntries().size());
    					cell.setColspan(1);
    				}
    				
    				
    				//cell lies in no mans land
    				else {
    					
    					for (int i=0; i<mainRow.getEntries().size(); i++){
    						
    						if (cell.getEnd().getX() < mainRow.getEntries().get(i).getStart().getX()){
    							boolean headerOnRight = false;
    							boolean headerOnLeft = false;
    							for (int j=0; j<row.getEntries().size(); j++){
    								
    								if (ifCellOverlaps(row.getEntries().get(j) , mainRow.getEntries().get(i))){
    									headerOnRight = true;    									   									
    								}
    								
    								if (ifCellOverlaps(row.getEntries().get(j), mainRow.getEntries().get(i-1))){
    									headerOnLeft = true;
    								}
    								
    							}
    							
    							if (headerOnRight && !headerOnLeft){
    								for (int j=0 ; j<row.getEntries().size(); j++){
    									if (ifCellOverlaps(row.getEntries().get(j) , mainRow.getEntries().get(i))){
    										
    										cell.setInit(i);
    										cell.setColspan(1);
    										
    									}
    								}
    							}
    							
    							else if (headerOnLeft && !headerOnRight){
    								for (int j=0 ; j<row.getEntries().size(); j++){
    									if (ifCellOverlaps(row.getEntries().get(j) , mainRow.getEntries().get(i))){
    										
    										cell.setInit(i+2);
    										cell.setColspan(1);
    										
    									}
    								}
    								
    							}
    							
    							else {
    								
    								float left = cell.getStart().getX() - mainRow.getEntries().get(i-1).getEnd().getX();
    								float right = mainRow.getEntries().get(i).getStart().getX() - cell.getEnd().getX();
    								
    								if (left <= right) {
    									cell.setInit(i);
    									cell.setColspan(1);
    								}
    								
    								else {
    									cell.setInit(i+1);
    									cell.setColspan(1);
    								}
    								
    								
    							}
    							
    						}
    						
    					}
    					
    					
    				}
    				
    				
    				
    				
    				
    			}
    			
    		}
    		
    	}
    	
//    	if (foundLeft){
//
//			for (TableRow sampleRow : table.getRows()){
//				
//				for (Cell incrementCells : sampleRow.getEntries()) {
//					
//					incrementCells.setInit(incrementCells.getInit() + 1);
//					
//				}
//				
//			}
//    	}   	
    	
	
    	//remove tentative footers based on cell span > 1
    	
    	if (Float.compare(pdfMeta.getTableEnd(), 0.0f) == 0){
    		if (!lineExtractor.getHorizontalLines().isEmpty()){
    			boolean footerHorizontalLineFound = textAndLines.findFooterLine(mainRow.getEntries().size(), table, lineExtractor.getHorizontalLines());
    			if (footerHorizontalLineFound){
            		for (int i=table.getRows().size()-1 ; i>=0 ; i--){
            			if (table.getRows().get(i).getEntries().get(0).getStart().getY() > textAndLines.getLastHorizontalLineCoordinate()){
            				table.removeRow(i);
            			}
            		}
            	}
    			else {
    				for (int i=table.getRows().size()-1 ; i>=0 ; i--){    		
                		if (table.getRows().get(i).getEntries().size()==1 && table.getRows().get(i).getEntries().get(0).getColspan() > 1){
                			table.removeRow(i);
                		}
                		else break;
                	}
    			}
    		}        	
        	else {
        		for (int i=table.getRows().size()-1 ; i>=0 ; i--){    		
            		if (table.getRows().get(i).getEntries().size()==1 && table.getRows().get(i).getEntries().get(0).getColspan() > 1){
            			table.removeRow(i);
            		}
            		else break;
            	}
        	}    		
    	}
    	
    	
    	
    	
    	//converting to ArrayList of array
    	
    	ArrayList<String[]> extractedTable = new ArrayList<String[]>();
    	
    	for (TableRow tableRow : table.getRows()){
    		String[] row = new String[mainRow.getEntries().size()];
    		for (int i=0; i<row.length; i++){
    			row[i] = "";
    		}
    		
    		for (Cell cell : tableRow.getEntries()){    			
    			for (int j = cell.getInit() - 1; j< cell.getInit() - 1 + cell.getColspan(); j++){
    				row [j] = row [j] + cell.getCellContent();
    			}
    		}
    		extractedTable.add(row);
    	}
    	
    	//Arranging the headers
    	    	
    	if (pdfMeta.getEndOfHeaderLine() != 0 && pdfMeta.getHeaderLines().isEmpty()){
    		for (int i=0 ; i<extractedTable.get(0).length; i++){
        		String content = "";
        		for (int j=0 ;j<=pdfMeta.getEndOfHeaderLine() ; j++){
        			if (!extractedTable.get(j)[i].replace(" ", "").isEmpty()){
        				if (content.replace(" ", "").isEmpty()){
        					content = extractedTable.get(j)[i];
        				}
        				else {
        					content = content + "|" + extractedTable.get(j)[i];
        				}
        			}    			
        		}
        		extractedTable.get(0)[i] = content;
        		
        	}
    		
    		for (int i=pdfMeta.getEndOfHeaderLine(); i>0 ; i--){
    			extractedTable.remove(i);
    		}
    	}
    	
//    	for (String[] row : extractedTable){
//    		for (String entry : row){
//    			System.out.print(entry + "----");
//    		}
//    		System.out.println();
//    	}
    	
    	
//    	HSSFWorkbook workbook = new HSSFWorkbook();
//    	HSSFSheet sheet = workbook.createSheet("Sample sheet");    	
//    	
//    	for (int i=0; i<table.getRows().size(); i++){
//    		HSSFRow row1 = sheet.createRow(i);
//    		for (Cell cell : table.getRows().get(i).getEntries()){
//    			HSSFCell cell1 = row1.createCell(cell.getInit()-1);
//    			String wordString = "";
//				for (Word word: cell.getEntries()){
//					wordString = wordString + word.getWord() + " ";
//				}
//    			if (cell.getColspan() == 1) {    				
//    				cell1.setCellValue(wordString);
//    			}
//    			
//    			else {
//    				for (int j=0; j<cell.getColspan(); j++){
//    					cell1 = row1.createCell(cell.getInit() - 1 + j);
//    					cell1.setCellValue(wordString);
//    				}
//    				
//    			}
//    		}
//    	}
    	
//    	for (TableRow row : table.getRows()){
//    		
//    		for (Cell cell: row.getEntries()){
//    			System.out.print("Cell No: " + cell.getInit() + " Cell span:" + cell.getColspan() + " Cell content: ");
//    			for (Word word: cell.getEntries()){
//    				System.out.print(word.getWord() + " ");
//    			}            	               	
//    			
//    			System.out.print("|");
//    		}
//    		
//    		System.out.println();
//    		
//    	}
//    	
//    	System.out.println();
    	
    	//setting header text for next page
    	if (pdfMeta.getHeaderLines().isEmpty()){
    		ArrayList<String> headerLines = new ArrayList<String>();
        	for (int i=0; i<=pdfMeta.getEndOfHeaderLine(); i++){
        		String line = "";
        		for (Cell cell : extraction.get(i).getEntries()){
        			line = line + cell.getCellContent();
        		}
        		headerLines.add(line);
        	}
        	pdfMeta.setHeaderLines(headerLines);
    	}	
    	
    	return extractedTable;
    }
    
    public Table createTable(ArrayList<TableRow> extraction){
    	Table table = new Table();
    	for (TableRow row : extraction){
    		TableRow finalRow = new TableRow();
    		for (Cell cell: row.getEntries()){
    			Cell finalCell = new Cell(cell);
    			int numberOfOverlaps = 0;
    			for (int i=0 ; i<mainRow.getEntries().size(); i++) {                				
    				if (ifCellOverlaps(cell,mainRow.getEntries().get(i))){                					
    					numberOfOverlaps++;
    					if (finalCell.getInit()==-1) finalCell.setInit(i+1);               					
    				}       				
    				
    			}
    			finalCell.setColspan(numberOfOverlaps);
    			
    			if (numberOfOverlaps == 1) {
    				
    				for (int i=0 ; i<mainRow.getEntries().size() ; i++){
    					
    					if (ifCellOverlaps(finalCell,mainRow.getEntries().get(i))){
    						
    						if (i==0){
    							if (finalCell.getEnd().getX()<mainRow.getEntries().get(i+1).getStart().getX() && finalCell.getEnd().getX()>mainRow.getEntries().get(i).getEnd().getX()) {
    								mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    							}
    							
    							if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX()){
    								mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    							}
    						}
    						
    						else if (i == mainRow.getEntries().size() - 1){
    							
    							if (finalCell.getEnd().getX() > mainRow.getEntries().get(i).getEnd().getX()){
    								mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    							}
    							
    							if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX() && finalCell.getStart().getX() > mainRow.getEntries().get(i-1).getEnd().getX()){
    								mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    							}
    							
    						}
    						
    						else {
    							
    							if (finalCell.getEnd().getX()<mainRow.getEntries().get(i+1).getStart().getX() && finalCell.getEnd().getX()>mainRow.getEntries().get(i).getEnd().getX()) {
    								mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    							}
    							
    							if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX() && finalCell.getStart().getX() > mainRow.getEntries().get(i-1).getEnd().getX()){
    								mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    							}
    							
    						}
    						
    						
    					}
    					
    					
    				}
    				
    				
    			}
    			 			              			
    			
    			finalRow.addEntry(finalCell);
    			
    		}
    		
    		table.addRow(finalRow);
    		
    	}
//    	for (TableRow row : table.getRows()){
//    		for (Cell cell : row.getEntries()){
//    			System.out.print("Cell start : " + cell.getInit() + " Cell span: " + cell.getColspan());
//    			for (Word word : cell.getEntries()){
//    				System.out.print(word.getWord() + " ");
//    			}
//    			System.out.print("|");
//    		}
//    		System.out.println();
//    	}
    	
    	int numberOfCellsNotAppended = 1;
    	while (numberOfCellsNotAppended !=0 && atleastOneCellAppendedPreviously){
    		atleastOneCellAppendedPreviously = false;
    		numberOfCellsNotAppended = 0;
    		for (TableRow row : table.getRows()){
        		
        		for (Cell cell : row.getEntries()){
        			if (cell.getInit()==-1) {                				
        				table = addToTable(table,cell);
        				numberOfCellsNotAppended ++;
        			}
        			
        		}
        		
        	}
    	}
    	
//    	for (TableRow row : table.getRows()){
//    		for (Cell cell : row.getEntries()){
//    			System.out.print("Cell start : " + cell.getInit() + " Cell span: " + cell.getColspan());
//    			for (Word word : cell.getEntries()){
//    				System.out.print(word.getWord() + " ");
//    			}
//    			System.out.print("|");
//    		}
//    		System.out.println();
//    	}    	
    	
    	return table;
    }
    
    
    
    
    public Table addToTable(Table table, Cell finalCell){
    		int numberOfOverlaps = 0;
    		for (int i=0 ; i<mainRow.getEntries().size(); i++) {                				
    			if (ifCellOverlaps(finalCell,mainRow.getEntries().get(i))){                					
    				numberOfOverlaps++;
    				if (finalCell.getInit()==-1) finalCell.setInit(i+1);               					
    			}       				
    			
    		}
    			
    		finalCell.setColspan(numberOfOverlaps);
    		
    		if (numberOfOverlaps == 1) {
    			
    			for (int i=0 ; i<mainRow.getEntries().size() ; i++){
    					
    				if (ifCellOverlaps(finalCell,mainRow.getEntries().get(i))){
    					
    					if (i==0){
    						if (finalCell.getEnd().getX()<mainRow.getEntries().get(i+1).getStart().getX() && finalCell.getEnd().getX()>mainRow.getEntries().get(i).getEnd().getX()) {
    							mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    						}
    						
    						if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX()){
    							mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    						}
    					}
    					
    					else if (i == mainRow.getEntries().size() - 1){
    						
    						if (finalCell.getEnd().getX() > mainRow.getEntries().get(i).getEnd().getX()){
    							mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    						}
    						
    						if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX() && finalCell.getStart().getX() > mainRow.getEntries().get(i-1).getEnd().getX()){
    							mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    						}
    						
    					}
    					
    					else {
    						
    						if (finalCell.getEnd().getX()<mainRow.getEntries().get(i+1).getStart().getX() && finalCell.getEnd().getX()>mainRow.getEntries().get(i).getEnd().getX()) {
    							mainRow.getEntries().get(i).getEnd().setX(finalCell.getEnd().getX());
    						}
    						
    						if (finalCell.getStart().getX() < mainRow.getEntries().get(i).getStart().getX() && finalCell.getStart().getX() > mainRow.getEntries().get(i-1).getEnd().getX()){
    							mainRow.getEntries().get(i).getStart().setX(finalCell.getStart().getX());
    						}
    						
    					}
    					
    					
    				}
    				
    				
    			}
    				
    				
    		}
    		
    		if (finalCell.getInit()!=-1){
    			for (TableRow row : table.getRows()){
        			
        			for (Cell replaceCell : row.getEntries()){
        				
        				if (replaceCell.getStart().getX() == finalCell.getStart().getX() && replaceCell.getStart().getY()==finalCell.getStart().getY()){
        					replaceCell.setInit(finalCell.getInit());
        					replaceCell.setStart(finalCell.getStart());
        				}
        				
        			}
        			
        		}
    			atleastOneCellAppendedPreviously = true;
    		}
    		
    		return table;
    		
    }
    
    protected TableRow findMainRow(ArrayList<TableRow> extraction){
		
		ArrayList<Integer> rowSizes = new ArrayList<Integer>();
		
		for (TableRow row : extraction){
			rowSizes.add(row.getEntries().size());
		}
		Collections.sort(rowSizes);
		
		int maxRowSize = rowSizes.get(rowSizes.size()-1);
		
		for (TableRow row: extraction){
			if (row.getEntries().size() == maxRowSize) return row;
		}
		
		return null;
   	    	
    }
    
    protected boolean ifCellOverlaps(Cell cell1, Cell cell2){
    	Float startCell1 = cell1.getStart().getX();
    	Float endCell1 = cell1.getEnd().getX();
    	Float startCell2 = cell2.getStart().getX();
    	Float endCell2 = cell2.getEnd().getX();
    	
    	if (!(endCell1<=startCell2 || startCell1>=endCell2)){
    		return true;
    	}
    	
    	else return false;   	
    	
    }
    
    public ArrayList<ArrayList<Word>> makeWords(ArrayList<ArrayList<TextPosition>> sortedCharacterList){
    	ArrayList<ArrayList<Word>> wordRows= new ArrayList<ArrayList<Word>>();
    	ArrayList<Word> words = new ArrayList<Word>();
    	
    	for (ArrayList<TextPosition> characterRow : sortedCharacterList){
			float yCoordinate = -1;
			if (characterRow.size()>0) {
				yCoordinate = characterRow.get(0).getY();
			}
			StringBuilder word = new StringBuilder();
			Word dummyWord = new Word();
			Coordinates startCoordinates = new Coordinates();
			Coordinates endCoordinates = new Coordinates();
			ArrayList<Word> wordRow = new ArrayList<Word>();
			for (int i=0; i<characterRow.size(); i++){
				if (characterRow.size()==1){
					word.append(characterRow.get(i).getCharacter());
					startCoordinates = new Coordinates();
					startCoordinates.setX(characterRow.get(i).getX());
					startCoordinates.setY(yCoordinate);   
					endCoordinates = new Coordinates();        						
					endCoordinates.setX(characterRow.get(i).getX() + characterRow.get(i).getWidth());
					endCoordinates.setY(yCoordinate + characterRow.get(i).getHeight());
					dummyWord = new Word();
					dummyWord.setStart(startCoordinates);
					dummyWord.setEnd(endCoordinates);
					dummyWord.setWord(word.toString());
					dummyWord.setWidthOfSpace(characterRow.get(i).getWidthOfSpace());
					wordRow.add(dummyWord);
					words.add(dummyWord);
					word.delete(0, word.length());
					break;
				}
				
				
				
				else {
					
					if (i == 0) {
    					word.append(characterRow.get(i).getCharacter());
						startCoordinates = new Coordinates();
    					startCoordinates.setX(characterRow.get(i).getX());
    					startCoordinates.setY(yCoordinate);		
    					
    				} else if (i == characterRow.size() - 1) {
    					Float left = characterRow.get(i).getX() - (characterRow.get(i-1).getX() + characterRow.get(i-1).getWidth());
    					if (left < 0 ) left = (float) 0.0;
    					
    					if (left < characterRow.get(i).getWidthOfSpace()/2){
    						word.append(characterRow.get(i).getCharacter());
        					endCoordinates = new Coordinates();        						
    						endCoordinates.setX(characterRow.get(i).getX() + characterRow.get(i).getWidth());
    						endCoordinates.setY(yCoordinate + characterRow.get(i).getHeight());
    						dummyWord = new Word();
    						dummyWord.setStart(startCoordinates);
    						dummyWord.setEnd(endCoordinates);
    						dummyWord.setWidthOfSpace(characterRow.get(i).getWidthOfSpace());
    						dummyWord.setWord(word.toString());
    						wordRow.add(dummyWord);
    						words.add(dummyWord);
    						word.delete(0, word.length());  
    					}
    					
    					else {
    						if (!(word.length()==0)){
    							endCoordinates = new Coordinates();        						
        						endCoordinates.setX(characterRow.get(i-1).getX() + characterRow.get(i-1).getWidth());
        						endCoordinates.setY(yCoordinate + characterRow.get(i-1).getHeight());
        						dummyWord = new Word();
        						dummyWord.setStart(startCoordinates);
        						dummyWord.setEnd(endCoordinates);
        						dummyWord.setWidthOfSpace(characterRow.get(i-1).getWidthOfSpace());
        						dummyWord.setWord(word.toString());
        						wordRow.add(dummyWord);
        						words.add(dummyWord);
        						word.delete(0, word.length());       							
    						}
    						word.append(characterRow.get(i).getCharacter());
    						startCoordinates = new Coordinates();
    						startCoordinates.setX(characterRow.get(i).getX());
    						startCoordinates.setY(yCoordinate);
            				endCoordinates = new Coordinates();        						
        					endCoordinates.setX(characterRow.get(i).getX() + characterRow.get(i).getWidth());
        					endCoordinates.setY(yCoordinate + characterRow.get(i).getHeight());
        					dummyWord = new Word();
        					dummyWord.setStart(startCoordinates);
        					dummyWord.setEnd(endCoordinates);
        					dummyWord.setWidthOfSpace(characterRow.get(i).getWidthOfSpace());
        					dummyWord.setWord(word.toString());
        					wordRow.add(dummyWord);
        					words.add(dummyWord);
        					word.delete(0, word.length());  
    						
    					}
    					      					        					
    				} else {
    					TextPosition current = characterRow.get(i);
    					Float left = current.getX() - (characterRow.get(i-1).getX() + characterRow.get(i-1).getWidth());
    					if (left < 0 ) left = (float) 0.0;
    					
    					if ( left < current.getWidthOfSpace()/2 ) word.append(current.getCharacter());
    					else {
    						endCoordinates = new Coordinates();        						
        					endCoordinates.setX(characterRow.get(i-1).getX() + characterRow.get(i-1).getWidth());
        					endCoordinates.setY(yCoordinate + characterRow.get(i-1).getHeight());
        					dummyWord = new Word();
        					dummyWord.setStart(startCoordinates);
        					dummyWord.setEnd(endCoordinates);
        					dummyWord.setWidthOfSpace(characterRow.get(i-1).getWidthOfSpace());
        					dummyWord.setWord(word.toString());
        					wordRow.add(dummyWord);
        					words.add(dummyWord);
        					word.delete(0, word.length());        						
    						word.append(current.getCharacter());
    						startCoordinates = new Coordinates();
        					startCoordinates.setX(current.getX());
        					startCoordinates.setY(yCoordinate);            					            						
    					}
    					
    				}
					
				}
				
			}
			
			if (wordRow.size()>0) wordRows.add(wordRow);
		}
		    		
		ArrayList<ArrayList<Word>> sortedWordRows = new ArrayList<ArrayList<Word>>(); 
		ArrayList<Float> yCoordinate = new ArrayList<Float>();
		for (Word word : words){
			yCoordinate.add(word.getStart().getY());
		}
    	Set<Float> uniqueYset = new HashSet<Float>(yCoordinate);
    	ArrayList<Float> uniqueY = new ArrayList<Float>(uniqueYset);
    	Collections.sort(uniqueY);
		
    	for (float yCoord : uniqueY){
    		ArrayList<Word> wordRow = new ArrayList<Word>();
    		for (Word word : words){
    			if (Float.compare(yCoord, word.getStart().getY())==0) {
    				wordRow.add(word);
    			}
    		}
    		
    		ArrayList<Float> xCoordinates = new ArrayList<Float>();
    		for (Word word : wordRow){
    			xCoordinates.add(word.getStart().getX());
    		}
    		Set<Float> uniqueXset = new HashSet<Float>(xCoordinates);
        	ArrayList<Float> uniqueX = new ArrayList<Float>(uniqueXset);
        	Collections.sort(uniqueX);
        	ArrayList<Word> sortedWordRow = new ArrayList<Word>();
        	for (float xCoord : uniqueX){
        		for (Word word : wordRow){
            		if (Float.compare(xCoord, word.getStart().getX())==0){
            			sortedWordRow.add(word);
            			break;
            		}
            	}
        	}           		
    		sortedWordRows.add(sortedWordRow);            		
    	}
    	return sortedWordRows;
    }
    
}
