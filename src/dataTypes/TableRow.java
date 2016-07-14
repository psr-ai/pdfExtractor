package dataTypes;

import java.util.ArrayList;

public class TableRow {
	private ArrayList<Cell> entries = new ArrayList<Cell>();
	

	public ArrayList<Cell> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Cell> entries) {
		this.entries = entries;
	}
	
	public void addEntry(Cell entry){
		entries.add(entry);
	}
	
}
