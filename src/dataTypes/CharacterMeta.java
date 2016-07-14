package dataTypes;

import org.apache.pdfbox.util.TextPosition;

public class CharacterMeta {
	private char character;
	private TextPosition textPosition;
	
	public CharacterMeta () {
		
	}
	
	public CharacterMeta (char character, TextPosition textPosition) {
		this.character = character;
		this.textPosition = textPosition;
	}
	
	public char getCharacter() {
		return character;
	}
	public void setCharacter(char character) {
		this.character = character;
	}
	
	public TextPosition getTextPosition() {
		return textPosition;
	}

	public void setTextPosition(TextPosition textPosition) {
		this.textPosition = textPosition;
	}
	
	
}
