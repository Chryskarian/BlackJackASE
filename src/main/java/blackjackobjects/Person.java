package blackjackobjects;

import java.util.ArrayList;
import java.util.List;

public class Person {

	private boolean bust;
	private List<Card> hand = new ArrayList<>();
	private int pointsOnHand;

	public void addHand(Card karte) {
		hand.add(karte);
	}

	public List<Card> getHand() {
		return hand;
	}

	public void delHand() {
		hand.clear();
	}

	public int getPointsOnHand() {
		return this.pointsOnHand;
	}

	public void setPointsOnHand(int points) {
		this.pointsOnHand = points;
	}

	public void addPointsOnHand(int add) {
		this.pointsOnHand += add;
	}

	public Card getLastCardOnHand() {
		return hand.get(hand.size() - 1);
	}

	public boolean isBust() {
		return bust;
	}

	public void setBust(boolean bust) {
		this.bust = bust;
	}
}
