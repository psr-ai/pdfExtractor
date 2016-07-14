package dataTypes;

import java.util.ArrayList;

public class Cell {
	private ArrayList<Word> entries = new ArrayList<Word>();
	private Coordinates start,end;
	private int init, colspan;
	private String cellContent;
	public Cell(){
		init = -1;
		colspan = -1;
	}
	
	public Cell(Cell cell){
		this.entries = new ArrayList<Word>(cell.entries);
		this.start = cell.getStart();
		this.end = cell.getEnd();
		this.init = cell.getInit();
		this.colspan = cell.getColspan();
		this.cellContent = cell.getCellContent();
	}
	
	
	
	public String getCellContent() {
		return cellContent;
	}

	public void setCellContent(String cellContent) {
		this.cellContent = cellContent;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public ArrayList<Word> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Word> entries) {
		this.entries = entries;
		start = entries.get(0).getStart();
		end = entries.get(entries.size()-1).getEnd();
		String content = "";
		for (Word word : entries){
			if (content.isEmpty()) content = word.getWord();
			else content = content + " " + word.getWord();
		}
		this.cellContent = content;
	}
	
	public void addEntry (Word word) {
		entries.add(word);
		start = entries.get(0).getStart();
		end = entries.get(entries.size()-1).getEnd();
		String content = "";
		for (Word word1 : entries){
			if (content.isEmpty()) content = word1.getWord();
			else content = content + " " + word1.getWord();
		}
		this.cellContent = content;
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
