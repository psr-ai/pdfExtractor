package extractTable;

import java.util.ArrayList;

import org.apache.pdfbox.util.TextPosition;

import dataTypes.CharacterMeta;

public class FindTextMeta {
	private Float startY;
	private Float endY;	
	
	public Float getStartY() {
		return startY;
	}
	public void setStartY(Float startY) {
		this.startY = startY;
	}
	public Float getEndY() {
		return endY;
	}
	public void setEndY(Float endY) {
		this.endY = endY;
	}
	public boolean ifPageContainsText(ArrayList<ArrayList<TextPosition>> sortedCharacterList, String headerText){
    	//String pageContent = "";
    	headerText = headerText.replace(" ", "").toLowerCase();
    	StringBuilder pageContentBuilder = new StringBuilder();
    	ArrayList<CharacterMeta> pageContentWithCoordinates = new ArrayList<CharacterMeta>();
    	for (ArrayList<TextPosition> characterRow : sortedCharacterList){
    		for (TextPosition text : characterRow){
    			for (int charPos =0; charPos <text.getCharacter().length(); charPos ++) {
    				pageContentBuilder.append(text.getCharacter().charAt(charPos));
    				pageContentWithCoordinates.add(new CharacterMeta(text.getCharacter().charAt(charPos), text));
    			}    			
    		}
    	}
    	
    	
    	String pageContent = pageContentBuilder.toString().replace(" ", "").toLowerCase();
    	
    	int startIndex = pageContent.indexOf(headerText);
    	if (startIndex >= 0) {
    		setStartY(pageContentWithCoordinates.get(startIndex).getTextPosition().getY());
    		setEndY(pageContentWithCoordinates.get(startIndex + headerText.length()-1).getTextPosition().getY());
    		return true;
    	}
    	
    	else return false;
    	
    	
//    	int pointer = 0;
    	
//    	if (pageContent.replace(" ", "").toLowerCase().contains(headerText.toLowerCase())) {
//    		for (int i = 0; i<pageContentWithCoordinates.size() ; i++){
//    			pointer = i;
//    			char pageCharacter = pageContentWithCoordinates.get(i).getCharacter().toLowerCase().charAt(0);
//    			char headerCharacter = headerText.charAt(0); 
//    			if (pageCharacter == headerCharacter){	
//    				float firstCharYPosition = pageContentWithCoordinates.get(i).getY();
//					for (int j=0 ; j<headerText.length();){
//						pageCharacter = pageContentWithCoordinates.get(i).getCharacter().toLowerCase().charAt(0);
//						headerCharacter = headerText.charAt(j);
//						
//						if (pageCharacter == headerCharacter) {
//							setEndY(pageContentWithCoordinates.get(i).getY());							
//							if (j == headerText.length()-1){
//								setStartY(firstCharYPosition);
//								
//								return true;
//							}
//							else if (j== 0){
//								firstCharYPosition = pageContentWithCoordinates.get(i).getY();
//							}
//							j+=pageContentWithCoordinates.get(i).getCharacter().length();
//							i++;
//						}
//						
//						else {
//							
//							break;
//						}
//						 
//					}
//					
//					i = pointer;
//				}
//			}
//    		return false;
//		}
//		
//	
//    	else return false;
    	
    }
}
