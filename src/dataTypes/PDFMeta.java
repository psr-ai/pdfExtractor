package dataTypes;

import java.util.ArrayList;

public class PDFMeta {
	private int pageRotation;
	private int fromPageNumber;
	private int toPageNumber;
	private int currentPageNumber;
	private String readingType;
	private float pageWidth;
	private float pageHeight;
	private ArrayList<SearchTextMeta> headerTextOnPages = new ArrayList<SearchTextMeta>();
	private ArrayList<SearchTextMeta> footerTextOnPages = new ArrayList<SearchTextMeta>();
	private float tableStart = 0.0f;
	private float tableEnd = 0.0f;
	private int endOfHeaderLine;	
	private ArrayList<String> headerLines = new ArrayList<String>();
			
	public ArrayList<String> getHeaderLines() {
		return headerLines;
	}
	public void setHeaderLines(ArrayList<String> headerLines) {
		this.headerLines = headerLines;
	}
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	
	public float getTableStart() {
		return tableStart;
	}
	public void setTableStart(float tableStart) {
		this.tableStart = tableStart;
	}
	public float getTableEnd() {
		return tableEnd;
	}
	public void setTableEnd(float tableEnd) {
		this.tableEnd = tableEnd;
	}
	public int getFromPageNumber() {
		return fromPageNumber;
	}
	public void setFromPageNumber(int fromPageNumber) {
		this.fromPageNumber = fromPageNumber;
	}
	public int getToPageNumber() {
		return toPageNumber;
	}
	public void setToPageNumber(int toPageNumber) {
		this.toPageNumber = toPageNumber;
	}
	public ArrayList<SearchTextMeta> getHeaderTextOnPages() {
		return headerTextOnPages;
	}
	public void setHeaderTextOnPages(ArrayList<SearchTextMeta> headerTextOnPages) {
		this.headerTextOnPages = headerTextOnPages;
	}
	public ArrayList<SearchTextMeta> getFooterTextOnPages() {
		return footerTextOnPages;
	}
	public void setFooterTextOnPages(ArrayList<SearchTextMeta> footerTextOnPages) {
		this.footerTextOnPages = footerTextOnPages;
	}
	public int getEndOfHeaderLine() {
		return endOfHeaderLine;
	}
	public void setEndOfHeaderLine(int endOfHeaderLine) {
		this.endOfHeaderLine = endOfHeaderLine;
	}
	
	public int getPageRotation() {
		return pageRotation;
	}
	public void setPageRotation(int pageRotation) {
		this.pageRotation = pageRotation;
	}
	public String getReadingType() {
		return readingType;
	}
	public void setReadingType(String readingType) {
		this.readingType = readingType;
	}
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
	
	public SearchTextMeta getHeaderTextMetaByPageNumber(int pageNumber){
		
		for (SearchTextMeta headerText : headerTextOnPages){
			if (pageNumber == headerText.getPageNumber()) return headerText;
		}
		return null;
		
	}
	
	public SearchTextMeta getFooterTextMetaByPageNumber(int pageNumber){
		for (SearchTextMeta footerText : footerTextOnPages){
			if (pageNumber == footerText.getPageNumber()) return footerText;
		}
		return null;
	}
	
	public void setCurrentPageMeta(int pageNumber){
		setCurrentPageNumber(pageNumber);
		boolean headerFound = false;
		boolean footerFound = false;
		for (SearchTextMeta headerText : headerTextOnPages){
			if (pageNumber == headerText.getPageNumber()) {
				setPageWidth(headerText.getPageWidth());
				setPageHeight(headerText.getPageHeight());
				setTableStart(headerText.getEndY());
				headerFound = true;
			}
			
		}
		if (!headerFound){
			setTableStart(0.0f);
		}
		for (SearchTextMeta footerText : footerTextOnPages){
			if (pageNumber == footerText.getPageNumber()) {
				setTableEnd(footerText.getStartY());
				footerFound = true;
			}
		}
		if (!footerFound){
			setTableEnd(0.0f);
		}
	}
	
}
