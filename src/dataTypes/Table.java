package dataTypes;

import java.util.ArrayList;

public class Table {
	ArrayList<TableRow> rows = new ArrayList<TableRow>();
	
	public ArrayList<TableRow> getRows() {
		return rows;
	}

	public void setRows(ArrayList<TableRow> rows) {
		this.rows = rows;
	}
	
	public void addRow(TableRow row){
		rows.add(row);
	}
	
	public void removeRow(int i){
		rows.remove(i);
	}
	
}
