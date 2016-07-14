package dataTypes;

public class TableLine {
	
	private Coordinates fromCoord = new Coordinates();
	private Coordinates toCoord = new Coordinates();
	private float scaledLength,scaledWidth;
	private boolean horizontalLine = false;
	private boolean verticalLine = false;
	public Coordinates getFromCoord() {
		return fromCoord;
	}
	public void setFromCoord(Coordinates fromCoord) {
		this.fromCoord = fromCoord;
	}
	public Coordinates getToCoord() {
		return toCoord;
	}
	public void setToCoord(Coordinates toCoord) {
		this.toCoord = toCoord;
	}
	public float getScaledWidth() {
		return scaledWidth;
	}
	public void setScaledWidth(float scaledWidth) {
		this.scaledWidth = scaledWidth;
	}
	public boolean isHorizontalLine() {
		return horizontalLine;
	}
	public void setHorizontalLine(boolean horizontalLine) {
		this.horizontalLine = horizontalLine;
	}
	public boolean isVerticalLine() {
		return verticalLine;
	}
	public void setVerticalLine(boolean verticalLine) {
		this.verticalLine = verticalLine;
	}
	public float getScaledLength() {
		return scaledLength;
	}
	public void setScaledLength(float scaledLength) {
		this.scaledLength = scaledLength;
	}
}
