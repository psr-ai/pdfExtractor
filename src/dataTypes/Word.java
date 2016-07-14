package dataTypes;

public class Word {
	
	private Coordinates start, end;
	private String word;
	private float widthOfSpace;
	
	
	public float getWidthOfSpace() {
		return widthOfSpace;
	}

	public void setWidthOfSpace(float widthOfSpace) {
		this.widthOfSpace = widthOfSpace;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Coordinates getStart() {
		return start;
	}

	public void setStart(Coordinates start) {
		this.start = start;
	}

	public Coordinates getEnd() {
		return end;
	}

	public void setEnd(Coordinates end) {
		this.end = end;
	}
	
	

}
