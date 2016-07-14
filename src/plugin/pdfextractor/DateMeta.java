package plugin.pdfextractor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.icu.text.SimpleDateFormat;

public class DateMeta {
	
	private String extractedDate;
	private String FormattedExtractedDate;
	private String[] selection;
	private String inputString;
	private ArrayList<String[]> dateFormats;
	private String dateFormat;
	private String selectedRegex;
	private String day;
	private String month;
	private String year;
	
	DateMeta(){
		dateFormats = new ArrayList<String[]>();
		selection = new String[3];
	}
	
	

	public String getFormattedExtractedDate() {
		return FormattedExtractedDate;
	}

	public void setFormattedExtractedDate(String formattedExtractedDate) {
		FormattedExtractedDate = formattedExtractedDate;
	}

	public String getDay() {
		return day;
	}





	public void setDay(String day) {
		this.day = day;
	}





	public String getMonth() {
		return month;
	}





	public void setMonth(String month) {
		this.month = month;
	}





	public String getYear() {
		return year;
	}





	public void setYear(String year) {
		this.year = year;
	}





	public String getSelectedRegex() {
		return selectedRegex;
	}







	public void setSelectedRegex(String selectedRegex) {
		this.selectedRegex = selectedRegex;
	}







	public String getDateFormat() {
		return dateFormat;
	}



	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}



	

	public ArrayList<String[]> getDateFormats() {
		return dateFormats;
	}



	public void setDateFormats(ArrayList<String[]> dateFormats) {
		this.dateFormats = dateFormats;
	}



	public String getInputString() {
		return inputString;
	}



	public void setInputString(String inputString) {
		this.inputString = inputString.toLowerCase();
	}

	

	public String[] getSelection() {
		return selection;
	}




	public void setSelection(String[] selection) {
		this.selection = selection;
	}




	public String getExtractedDate() {
		return extractedDate;
	}

	public void setExtractedDate(String extractedDate) {
		this.extractedDate = extractedDate;
	}

	public void determineDateFormat(){
			
		ArrayList<String> days = new ArrayList<String>();
		days.add("0[1-9]|[1 2][0-9]|[1-9]|[3][0-1]");
		days.add("0[1-9]st|[1 2][0-9]st|[1-9]st|[3][0-1]st");
		days.add("0[1-9]nd|[1 2][0-9]nd|[1-9]nd|[3][0-1]nd");
		days.add("0[1-9]rd|[1 2][0-9]rd|[1-9]rd|[3][0-1]rd");
		days.add("0[1-9]th|[1 2][0-9]th|[1-9]th|[3][0-1]th");
		
		
		ArrayList<String> months = new ArrayList<String>();
		months.add("0[1-9]|[1][0 1 2]|[1-9]");
		months.add("january");
		months.add("february");
		months.add("march");
		months.add("april");
		months.add("may");
		months.add("june");
		months.add("july");
		months.add("august");
		months.add("september");
		months.add("october");
		months.add("november");
		months.add("december");
		
		months.add("jan");
		months.add("feb");
		months.add("mar");
		months.add("apr");
		months.add("may");
		months.add("jun");
		months.add("jul");
		months.add("aug");
		months.add("sep");
		months.add("oct");
		months.add("nov");
		months.add("dec");
		
		ArrayList<String> years = new ArrayList<String>();
		years.add("[0-9]{4}|[0-9]{2}");
		
		//Format if user doen't give any separators
		ArrayList<String> singleDays = new ArrayList<String>();		
		singleDays.add("0[1-9]|[1 2][1-9]|[3][0 1]");
		
		ArrayList<String> singleMonths = new ArrayList<String>();
		singleMonths.add("[0-9][1 2]");
		singleMonths.add("january");
		singleMonths.add("february");
		singleMonths.add("march");
		singleMonths.add("april");
		singleMonths.add("may");
		singleMonths.add("june");
		singleMonths.add("july");
		singleMonths.add("august");
		singleMonths.add("september");
		singleMonths.add("october");
		singleMonths.add("november");
		singleMonths.add("december");
		
		singleMonths.add("jan");
		singleMonths.add("feb");
		singleMonths.add("mar");
		singleMonths.add("apr");
		singleMonths.add("may");
		singleMonths.add("jun");
		singleMonths.add("jul");
		singleMonths.add("aug");
		singleMonths.add("sep");
		singleMonths.add("oct");
		singleMonths.add("nov");
		singleMonths.add("dec");
		
		ArrayList<String> singleYears = new ArrayList<String>();
		singleYears.add("[1-9][0-9]{3}");
		
		HashMap<String, String> formats = new HashMap<String, String>();
		
		formats.put("({d})[,-/\\ ]+({m})[,-/\\ ]+({y})", "Date Month Year");
		formats.put("({d})[,-/\\ ]+({y})[,-/\\ ]+({m})", "Date Year Month");
		formats.put("({m})[,-/\\ ]+({y})[,-/\\ ]+({d})", "Month Year Date");
		formats.put("({m})[,-/\\ ]+({d})[,-/\\ ]+({y})", "Month Date Year");
		formats.put("({y})[,-/\\ ]+({m})[,-/\\ ]+({d})", "Year Month Date");
		formats.put("({y})[,-/\\ ]+({d})[,-/\\ ]+({m})", "Year Date Month");

		formats.put("({dn})({mn})({yn})", "Date Month Year");
		formats.put("({dn})({yn})({mn})", "Date Year Month");
		formats.put("({mn})({yn})({dn})", "Month Year Date");
		formats.put("({mn})({dn})({yn})", "Month Date Year");
		formats.put("({yn})({mn})({dn})", "Year Month Date");
		formats.put("({yn})({dn})({mn})", "Year Date Month");
		
		HashMap<String,String> datePatterns = new HashMap<String,String>();
		Set<?> set = formats.entrySet();
	    Iterator<?> iterator = set.iterator();
	    while(iterator.hasNext()) {
	       @SuppressWarnings("rawtypes")
		Map.Entry mentry = (Map.Entry)iterator.next();
	       for (String d : days) {
				for (String m : months) {
					for (String y : years) {
						for (String dn: singleDays){
							for (String mn: singleMonths){
								for(String yn: singleYears){
									datePatterns.put( mentry.getKey().toString().replace("{d}", d).replace("{m}", m).replace("{y}", y).replace("{dn}", dn).replace("{mn}", mn).replace("{yn}", yn), mentry.getValue().toString());
								}
							}
						}												
					}
					
				}
				
			}
	    }
	    
	    
		Set<?> datePatternSet = datePatterns.entrySet();
		Iterator<?> datePatternIterator = datePatternSet.iterator();
		
		ArrayList<String[]> detectedFormats = new ArrayList<String[]>();
		while (datePatternIterator.hasNext()){
			@SuppressWarnings("rawtypes")
			Map.Entry mentry = (Map.Entry) datePatternIterator.next();
			Pattern p = Pattern.compile(mentry.getKey().toString());
			Matcher m = p.matcher(getInputString());
			if (m.find()){
				if (Pattern.matches("^" + mentry.getKey().toString() + "$", m.group())){
					String[] format = new String[3];
					format[0] = m.group();						//Date
					format[1] = mentry.getValue().toString();	//Format
					format[2] = mentry.getKey().toString();		//Regex
					detectedFormats.add(format);
				}
			}
		}
				
		ArrayList<String> onlyString = new ArrayList<String>();
		for (String[] detectedFormat : detectedFormats){
			onlyString.add(detectedFormat[0]);
		}
			
		int max = 0;
		for (int i=0; i<onlyString.size(); i++){
			if (onlyString.get(i).length()>max) max = onlyString.get(i).length();
		}
		
		ArrayList<String[]> finalDetectedFormats = new ArrayList<String[]>();
		for (String [] detectedFormat : detectedFormats){
			if (detectedFormat[0].length()==max) finalDetectedFormats.add(detectedFormat);
		}			
		setDateFormats(finalDetectedFormats);
				
	}
	
	public void extractDate() throws ParseException{
		setExtractedDate(getSelection()[0]);
		setDateFormat(getSelection()[1]);
				
		Pattern p = Pattern.compile(getSelection()[2]);
		Matcher m = p.matcher(getExtractedDate());
		
		if (m.matches()){
			String[] format = getDateFormat().split(" ");
			
			if (format[0].equals("Date")){
				setDay(m.group(1));
				if (format[1].equals("Month")){
					setMonth(m.group(2));
					setYear(m.group(3));
				}
				
				else {
					setYear(m.group(2));
					setMonth(m.group(3));
				}
			}
			
			else if (format[0].equals("Month")){
				setMonth(m.group(1));
				if (format[1].equals("Date")){
					setDay(m.group(2));
					setYear(m.group(3));
				}
				
				else{
					setYear(m.group(2));
					setDay(m.group(3));
				}
			}
			
			else{
				setYear(m.group(1));
				if (format[1].equals("Date")){
					setDay(m.group(2));
					setMonth(m.group(3));
				}
				else {
					setMonth(m.group(2));
					setDay(m.group(3));
				}		
				
			}
		}
		
		convertToDateTime();
		
		
	}
	
	public void convertToDateTime() throws ParseException{
		try{
			Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(getMonth());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int month = cal.get(Calendar.MONTH) + 1;
			setMonth(String.valueOf(month));
		}
		
		catch(Exception e){
			setMonth(getMonth());
		}
		
		
		if (getDay().length()==1) setDay("0" + getDay());
		
		if (getYear().length()==2){
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			String century = year.substring(0,2);
			year = year.substring(year.length()-2, year.length());			
			int currentYear = Integer.parseInt(year);
			
			int dateYear = Integer.parseInt(getYear());
			
			if (dateYear>=0 && dateYear<=currentYear){
				setYear(century + getYear());
			}
			else{
				int previousCentury = Integer.parseInt(century) - 1;
				setYear(String.valueOf(previousCentury) + getYear());
			}
		}
		
		if (getMonth().length()==1) setMonth("0" + getMonth());
		
		setFormattedExtractedDate(getMonth() + "-" + getDay() + "-" + getYear() + " 00:00:00");
		
		
			
	}
	public void processDateMeta(String input, String dateFormat) throws ParseException{
		setInputString(input);
		setDateFormat(dateFormat);
		try {
			
			determineDateFormat();
			if (getDateFormats().size()==1){
				setSelection(getDateFormats().get(0));
				extractDate();
			}
			
			else{
				for (String[] format : getDateFormats()){
					if (format[1] == dateFormat) {
						setSelection(format);
						break;
					}
				}
				
				extractDate();
			}
		}
		catch (NullPointerException e){
			day = null;
			month = null;
			year = null;
		}
		
		
	}
	
	public ArrayList<String[]> processDateMeta(String input){
		try {
			setInputString(input);
			determineDateFormat();
			return getDateFormats();
		}
		catch (NullPointerException e){
			return null;
		}	
		
	}
	
	
	
	
//	public static void main(String[] args) throws ParseException{
//		DateMeta myDate = new DateMeta();
//		myDate.processDateMeta("10:00 AM ET, Wednesday, January 6, 2016");
//		System.out.println(myDate.getFormattedExtractedDate());
//		
		
		
		
//		myDate.determineDateFormat();
//		System.out.println("Options: " + myDate.getDateFormats().size());
//		myDate.setSelection(myDate.getDateFormats().get(0));
//		myDate.extractDate();
//		System.out.println("Date:" + myDate.getExtractedDate());
//		System.out.println("Date Format:" + myDate.getDateFormat());
//		System.out.println("Day:" + myDate.getDay());
//		System.out.println("Month:" + myDate.getMonth());
//		System.out.println("Year:" + myDate.getYear());
		
//	}
	
}
