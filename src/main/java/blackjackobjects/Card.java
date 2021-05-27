package blackjackobjects;

import javax.swing.ImageIcon;

public class Card {

	private ImageIcon cardImage;
	private int cardValue;
	private String cardColour;

	public Card(String cardColour, int cardValue, ImageIcon cardImage) {
		this.cardValue = cardValue;
		this.cardImage = cardImage;
		this.cardColour = cardColour;
	}

	public int getCardValue() {
		return this.cardValue;
	}

	public String getCardColour() {
		return this.cardColour;
	}

	public ImageIcon getCardImage() {
		return this.cardImage;
	}

	public void switchAce() {
		this.cardValue = 1;
	}
}
