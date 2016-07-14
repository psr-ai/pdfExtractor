package plugin.pdfextractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class DateExtractorPDF {
	private String inputText;
	private String filePath;
	private DateMeta extractedDate;
	private String dateFormat;
	private String preLabel;
	private int pageNumber;
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public DateMeta getExtractedDate() {
		return extractedDate;
	}
	public void setExtractedDate(DateMeta extractedDate) {
		this.extractedDate = extractedDate;
	}
	public String getPreLabel() {
		return preLabel;
	}
	public void setPreLabel(String preLabel) {
		this.preLabel = preLabel;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getInputText() {
		return inputText;
	}
	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	
	
	public DateMeta processPDFDate(String filePath, String preLabel) throws Exception {
		setPreLabel(preLabel);
		setFilePath(filePath);
		PDDocument document = null;
	    File input = new File(filePath);
	    document = PDDocument.load(input);
	    if (document.isEncrypted()) {
	    	document.decrypt("");
	    }
	    document.setAllSecurityToBeRemoved(true);
	    @SuppressWarnings("unchecked")
		List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
	    int pages = allPages.size();
	    
	    for (int i=0; i<pages; i++){
	    	PrintTextLocations pageExtractor = new PrintTextLocations();
	    	ArrayList<String> textRows = pageExtractor.pdfPageExtractor(filePath, i);
	    	for (int j=0; j<textRows.size(); j++){
	    		if (textRows.get(j).contains(getPreLabel())){
	    			DateMeta myDate = new DateMeta();
	    			myDate.processDateMeta(textRows.get(j).substring(textRows.get(j).indexOf(getPreLabel()) + getPreLabel().length(), textRows.get(j).length()));
	    			if (myDate.getDateFormats().size()!=0){
	    				setExtractedDate(myDate);
	    				setPageNumber(i);
	    				return myDate;
	    			}
	    			
	    			else {
	    				for (int k=j+1; k<j+1+2; k++){
	    					if (k!=textRows.size()+1){
	    						myDate = new DateMeta();
	    						myDate.processDateMeta(textRows.get(k));
	    		    			if (myDate.getDateFormats().size()!=0){
	    		    				setExtractedDate(myDate);
	    		    				setPageNumber(i);
	    		    				return myDate;
	    		    			}
	    					}    					
	    				}
	    			}
	    			
	    			
	    		}
	    	}    	
	    	
	    	
	    }
	    return null;
		
	}
	
	public DateMeta processPDFDate(String filePath, String preLabel, String dateFormat) throws Exception {
		setPreLabel(preLabel);
		setFilePath(filePath);
		PDDocument document = null;
	    File input = new File(filePath);
	    document = PDDocument.load(input);
	    if (document.isEncrypted()) {
	    	document.decrypt("");
	    }
	    document.setAllSecurityToBeRemoved(true);
	    @SuppressWarnings("unchecked")
		List<PDPage> allPages = document.getDocumentCatalog().getAllPages();
	    int pages = allPages.size();
	    
	    for (int i=0; i<pages; i++){
	    	PrintTextLocations pageExtractor = new PrintTextLocations();
	    	ArrayList<String> textRows = pageExtractor.pdfPageExtractor(filePath, i);
	    	for (int j=0; j<textRows.size(); j++){
	    		if (textRows.get(j).contains(getPreLabel())){
	    			DateMeta myDate = new DateMeta();
	    			myDate.processDateMeta(textRows.get(j).substring(textRows.get(j).indexOf(getPreLabel()) + getPreLabel().length(), textRows.get(j).length()), dateFormat);
	    			if (myDate.getDateFormats().size()!=0){
	    				setExtractedDate(myDate);
	    				setPageNumber(i);
	    				return myDate;
	    			}
	    			
	    			else {
	    				for (int k=j+1; k<j+1+2; k++){
	    					if (k!=textRows.size()+1){
	    						myDate = new DateMeta();
	    						myDate.processDateMeta(textRows.get(k), dateFormat);
	    		    			if (myDate.getDateFormats().size()!=0){
	    		    				setExtractedDate(myDate);
	    		    				setPageNumber(i);
	    		    				return myDate;
	    		    			}
	    					}    					
	    				}
	    			}
	    			
	    			
	    		}
	    	}    	
	    	
	    	
	    }
	    return null;
		
	}
	
	public DateMeta processPDFDate(String filePath, int pageNumber, String preLabel, String dateFormat) throws Exception {
		setPreLabel(preLabel);
		setFilePath(filePath);
		setDateFormat(dateFormat);
		
		PDDocument document = null;
	    File input = new File(filePath);
	    document = PDDocument.load(input);
	    if (document.isEncrypted()) {
	    	document.decrypt("");
	    }
	    document.setAllSecurityToBeRemoved(true);
		PrintTextLocations pageExtractor = new PrintTextLocations();
	    ArrayList<String> textRows = pageExtractor.pdfPageExtractor(filePath, pageNumber);
	    for (int j=0; j<textRows.size(); j++){
	    	if (textRows.get(j).contains(getPreLabel())){
	    		DateMeta myDate = new DateMeta();
	    		myDate.processDateMeta(textRows.get(j).substring(textRows.get(j).indexOf(getPreLabel()) + getPreLabel().length(), textRows.get(j).length()),getDateFormat());
	    		if (myDate.getDay()!=null){
	    			setExtractedDate(myDate);
	    			setPageNumber(pageNumber);
	    			return myDate;
	    		}
	    		
	    		else {
	    			for (int k=j+1; k<j+1+2; k++){
	    				if (k!=textRows.size()+1){
	    					myDate = new DateMeta();
	    					myDate.processDateMeta(textRows.get(k), getDateFormat());
	    	    			if (myDate.getDay()!=null){
	    	    				setPageNumber(pageNumber);
	    	    				setExtractedDate(myDate);
	    	    				return myDate;
	    	    			}
	    				}    					
	    			}
	    		}
	    		
	    			
	    	}
	    }    	
	    	
	    	
	    return null;
		
	}
	
	
//	public static void main(String args[]) throws Exception{
//		DateExtractorPDF test = new DateExtractorPDF();
//		test.processPDFDate("E:\\Analytics Practice\\testing pdf\\empsit.pdf", 0, "Transmission of material ", "Month Date Year");
//		for (String[] output : test.getExtractedDate().getDateFormats()){
//			System.out.println(output[1]);
//		}
//	}
	
}
