package blackjackobjects;

import java.util.Collections;
import java.util.Stack;
import javax.swing.ImageIcon;

public class PlayingCardDeck {

	private Stack<Card> deck;

	public PlayingCardDeck() {
		createPlayingCardDeck();
	}

	private final void createPlayingCardDeck() {

		this.deck = new Stack<>();

		// Lädt die Bilder für alle Pik
		ImageIcon pikImage2 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/2_of_spades.png");
		ImageIcon pikImage3 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/3_of_spades.png");
		ImageIcon pikImage4 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/4_of_spades.png");
		ImageIcon pikImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/5_of_spades.png");
		ImageIcon pikImage6 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/6_of_spades.png");
		ImageIcon pikImage7 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/7_of_spades.png");
		ImageIcon pikImage8 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/8_of_spades.png");
		ImageIcon pikImage9 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/9_of_spades.png");
		ImageIcon pikImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Pik/10_of_spades.png");
		ImageIcon pikImageB = new ImageIcon("src/main/resources/KartenDeckImages/Pik/jack_of_spades2.png");
		ImageIcon pikImageD = new ImageIcon("src/main/resources/KartenDeckImages/Pik/queen_of_spades2.png");
		ImageIcon pikImageK = new ImageIcon("src/main/resources/KartenDeckImages/Pik/king_of_spades2.png");
		ImageIcon pikImageA = new ImageIcon("src/main/resources/KartenDeckImages/Pik/ace_of_spades2.png");

		// Erstellt die Karten der Farbe Pik
		Card pik2 = new Card("2", 2, pikImage2);
		Card pik3 = new Card("3", 3, pikImage3);
		Card pik4 = new Card("4", 4, pikImage4);
		Card pik5 = new Card("5", 5, pikImage5);
		Card pik6 = new Card("6", 6, pikImage6);
		Card pik7 = new Card("7", 7, pikImage7);
		Card pik8 = new Card("8", 8, pikImage8);
		Card pik9 = new Card("9", 9, pikImage9);
		Card pik10 = new Card("10", 10, pikImage10);
		Card pikB = new Card("B", 10, pikImageB);
		Card pikD = new Card("D", 10, pikImageD);
		Card pikK = new Card("K", 10, pikImageK);
		Card pikA = new Card("A", 11, pikImageA);

		// Fügt die Karten Pik dem Deck hinzu
		this.deck.add(pik2);
		this.deck.add(pik3);
		this.deck.add(pik4);
		this.deck.add(pik5);
		this.deck.add(pik6);
		this.deck.add(pik7);
		this.deck.add(pik8);
		this.deck.add(pik9);
		this.deck.add(pik10);
		this.deck.add(pikB);
		this.deck.add(pikD);
		this.deck.add(pikK);
		this.deck.add(pikA);

		// Lädt die Bilder für die Farbe Herz
		ImageIcon herzImage2 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/2_of_hearts.png");
		ImageIcon herzImage3 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/3_of_hearts.png");
		ImageIcon herzImage4 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/4_of_hearts.png");
		ImageIcon herzImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/5_of_hearts.png");
		ImageIcon herzImage6 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/6_of_hearts.png");
		ImageIcon herzImage7 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/7_of_hearts.png");
		ImageIcon herzImage8 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/8_of_hearts.png");
		ImageIcon herzImage9 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/9_of_hearts.png");
		ImageIcon herzImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Herz/10_of_hearts.png");
		ImageIcon herzImageB = new ImageIcon("src/main/resources/KartenDeckImages/Herz/jack_of_hearts2.png");
		ImageIcon herzImageD = new ImageIcon("src/main/resources/KartenDeckImages/Herz/queen_of_hearts2.png");
		ImageIcon herzImageK = new ImageIcon("src/main/resources/KartenDeckImages/Herz/king_of_hearts2.png");
		ImageIcon herzImageA = new ImageIcon("src/main/resources/KartenDeckImages/Herz/ace_of_hearts.png");

		// Erstellt die Karten der Farbe Herz
		Card herz2 = new Card("2", 2, herzImage2);
		Card herz3 = new Card("3", 3, herzImage3);
		Card herz4 = new Card("4", 4, herzImage4);
		Card herz5 = new Card("5", 5, herzImage5);
		Card herz6 = new Card("6", 6, herzImage6);
		Card herz7 = new Card("7", 7, herzImage7);
		Card herz8 = new Card("8", 8, herzImage8);
		Card herz9 = new Card("9", 9, herzImage9);
		Card herz10 = new Card("10", 10, herzImage10);
		Card herzB = new Card("B", 10, herzImageB);
		Card herzD = new Card("D", 10, herzImageD);
		Card herzK = new Card("K", 10, herzImageK);
		Card herzA = new Card("A", 11, herzImageA);

		// Fügt die Karten der Farbe Herz zum Deck
		this.deck.add(herz2);
		this.deck.add(herz3);
		this.deck.add(herz4);
		this.deck.add(herz5);
		this.deck.add(herz6);
		this.deck.add(herz7);
		this.deck.add(herz8);
		this.deck.add(herz9);
		this.deck.add(herz10);
		this.deck.add(herzB);
		this.deck.add(herzD);
		this.deck.add(herzK);
		this.deck.add(herzA);

		// Lädt die Bilder für die Farbe kreuz
		ImageIcon kreuzImage2 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/2_of_clubs.png");
		ImageIcon kreuzImage3 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/3_of_clubs.png");
		ImageIcon kreuzImage4 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/4_of_clubs.png");
		ImageIcon kreuzImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/5_of_clubs.png");
		ImageIcon kreuzImage6 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/6_of_clubs.png");
		ImageIcon kreuzImage7 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/7_of_clubs.png");
		ImageIcon kreuzImage8 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/8_of_clubs.png");
		ImageIcon kreuzImage9 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/9_of_clubs.png");
		ImageIcon kreuzImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/10_of_clubs.png");
		ImageIcon kreuzImageB = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/jack_of_clubs2.png");
		ImageIcon kreuzImageD = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/queen_of_clubs2.png");
		ImageIcon kreuzImageK = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/king_of_clubs2.png");
		ImageIcon kreuzImageA = new ImageIcon("src/main/resources/KartenDeckImages/Kreuz/ace_of_clubs.png");

		// Erstellt die Karten der Farbe Kreuz
		Card kreuz2 = new Card("2", 2, kreuzImage2);
		Card kreuz3 = new Card("3", 3, kreuzImage3);
		Card kreuz4 = new Card("4", 4, kreuzImage4);
		Card kreuz5 = new Card("5", 5, kreuzImage5);
		Card kreuz6 = new Card("6", 6, kreuzImage6);
		Card kreuz7 = new Card("7", 7, kreuzImage7);
		Card kreuz8 = new Card("8", 8, kreuzImage8);
		Card kreuz9 = new Card("9", 9, kreuzImage9);
		Card kreuz10 = new Card("10", 10, kreuzImage10);
		Card kreuzB = new Card("B", 10, kreuzImageB);
		Card kreuzD = new Card("D", 10, kreuzImageD);
		Card kreuzK = new Card("K", 10, kreuzImageK);
		Card kreuzA = new Card("A", 11, kreuzImageA);

		// Fügt die Karten der Farbe Kreuz dem Deck hinzu
		this.deck.add(kreuz2);
		this.deck.add(kreuz3);
		this.deck.add(kreuz4);
		this.deck.add(kreuz5);
		this.deck.add(kreuz6);
		this.deck.add(kreuz7);
		this.deck.add(kreuz8);
		this.deck.add(kreuz9);
		this.deck.add(kreuz10);
		this.deck.add(kreuzB);
		this.deck.add(kreuzD);
		this.deck.add(kreuzK);
		this.deck.add(kreuzA);

		// Lädt die Bilder fuer die Farbe Karo
		ImageIcon karoImage2 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/2_of_diamonds.png");
		ImageIcon karoImage3 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/3_of_diamonds.png");
		ImageIcon karoImage4 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/4_of_diamonds.png");
		ImageIcon karoImage5 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/5_of_diamonds.png");
		ImageIcon karoImage6 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/6_of_diamonds.png");
		ImageIcon karoImage7 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/7_of_diamonds.png");
		ImageIcon karoImage8 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/8_of_diamonds.png");
		ImageIcon karoImage9 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/9_of_diamonds.png");
		ImageIcon karoImage10 = new ImageIcon("src/main/resources/KartenDeckImages/Karo/10_of_diamonds.png");
		ImageIcon karoImageB = new ImageIcon("src/main/resources/KartenDeckImages/Karo/jack_of_diamonds2.png");
		ImageIcon karoImageD = new ImageIcon("src/main/resources/KartenDeckImages/Karo/queen_of_diamonds2.png");
		ImageIcon karoImageK = new ImageIcon("src/main/resources/KartenDeckImages/Karo/king_of_diamonds2.png");
		ImageIcon karoImageA = new ImageIcon("src/main/resources/KartenDeckImages/Karo/ace_of_diamonds.png");

		// Erstellt die Karten der Farbe Karo
		Card karo2 = new Card("2", 2, karoImage2);
		Card karo3 = new Card("3", 3, karoImage3);
		Card karo4 = new Card("4", 4, karoImage4);
		Card karo5 = new Card("5", 5, karoImage5);
		Card karo6 = new Card("6", 6, karoImage6);
		Card karo7 = new Card("7", 7, karoImage7);
		Card karo8 = new Card("8", 8, karoImage8);
		Card karo9 = new Card("9", 9, karoImage9);
		Card karo10 = new Card("10", 10, karoImage10);
		Card karoB = new Card("B", 10, karoImageB);
		Card karoD = new Card("D", 10, karoImageD);
		Card karoK = new Card("K", 10, karoImageK);
		Card karoA = new Card("A", 11, karoImageA);

		// Fügt die Karten der Farbe Karo dem Deck hinzu
		this.deck.add(karo2);
		this.deck.add(karo3);
		this.deck.add(karo4);
		this.deck.add(karo5);
		this.deck.add(karo6);
		this.deck.add(karo7);
		this.deck.add(karo8);
		this.deck.add(karo9);
		this.deck.add(karo10);
		this.deck.add(karoB);
		this.deck.add(karoD);
		this.deck.add(karoK);
		this.deck.add(karoA);

		// Mischt das Deck
		Collections.shuffle(deck);
	}

	public Card getCard() {
		return deck.pop();
	}

	public Card viewCard() {
		return deck.peek();
	}
}
