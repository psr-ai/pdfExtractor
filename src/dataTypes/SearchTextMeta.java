package dataTypes;

public class SearchTextMeta {
	private int pageNumber;
	private float startY;
	private float endY;
	private float pageWidth;
	private float pageHeight;
	
	public SearchTextMeta(){
		pageNumber = -1;
	}
	
	public SearchTextMeta(SearchTextMeta searchText){
		this.pageNumber = searchText.pageNumber;
		this.startY = searchText.startY;
		this.endY = searchText.endY;
		this.pageWidth = searchText.pageWidth;
		this.pageHeight = searchText.pageHeight;
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
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public float getStartY() {
		return startY;
	}
	public void setStartY(float startY) {
		this.startY = startY;
	}
	public float getEndY() {
		return endY;
	}
	public void setEndY(float endY) {
		this.endY = endY;
	}
	
}
