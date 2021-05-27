package model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import blackjackobjects.Card;
import blackjackobjects.Person;
import blackjackobjects.Money;
import blackjackobjects.PlayingCardDeck;
import databasecommunication.HibernateDatabaseActions;
import databasecommunication.Password;
import databasecommunication.Player;

public class BlackJackGame {
	private final Logger logger = Logger.getLogger(BlackJackGame.class.getName());
	private Player player;
	private Person croupier;
	private PlayingCardDeck deck;

	public BlackJackGame(Player player, Person coupier) {
		this.player = player;
		this.croupier = coupier;
	}

	public enum roundType {
		SPLIT, DOPPEL, NORMAL, BLACKJACK;
	}

	public enum winSituation {
		SPIELERGEWINNT, COUPIERGEWINNT, H1, H2, H1H2, H0, UNENTSCHIEDEN;
	}

	public enum overdrawn {
		JA, NEIN, HAND2JA, HAND2NEIN;
	}

	public roundType startRound(double bet) {
		reset();
		deck = new PlayingCardDeck();
		placeBet(bet);
		givePlayingCard(this.croupier);
		givePlayingCard(this.player);
		givePlayingCard(this.player);
		return evaluateStart();
	}

	public String stopRound() {
		if (player.isSplit()) {
			if (player.isHand2()) {
				croupierTurn();
				return evaluateStopSplit();
			}
			player.setHand2(true);
			return "Hand 2 wird gespielt";
		}
		croupierTurn();
		return evaluateStopNormal();
	}

	public void splitRound() {
		player.setSplitPlayerPoints(player.getLastCardOnHand().getCardValue());
		player.setPointsOnHand(player.getPointsOnHand() - player.getLastCardOnHand().getCardValue());
		player.addToSplitPlayerHand(player.getLastCardOnHand());
		player.getHand().remove(player.getLastCardOnHand());
		player.removeFromBalance(player.getBetAmount());
		player.setBetAmount(Money.multiply(player.getBetAmount(), 2));
		player.setSplit(true);
	}

	public String doubleRound() {
		player.removeFromBalance(player.getBetAmount());
		player.setBetAmount(Money.multiply(player.getBetAmount(), 2));
		givePlayingCard(player);
		croupierTurn();
		return evaluateStopNormal();
	}

	private void croupierTurn() {
		do {
			givePlayingCard(croupier);
		} while (croupier.getPointsOnHand() < 17);
		checkIfBust(croupier);
	}

	public String blackjack() {
		givePlayingCard(croupier);
		if (croupier.getPointsOnHand() == 21) {
			return "Sie und Croupier haben einen BlackJack. Einsatz zurück.";
		}
		player.addToBalance(Money.multiply(player.getBetAmount(), 3));
		return "Sie gewinnen mit einem BlackJack. 3-Fachen Einsatz zurück.";
	}

	public overdrawn givePlayingCard(Person person) {
		checkForAce(person);
		Card card = deck.getCard();
		if (person instanceof Player && ((Player) person).isHand2()) {
			((Player) person).addToSplitPlayerHand(card);
			((Player) person).addSplitPlayerPoints(card.getCardValue());
		} else {
			person.addHand(card);
			person.addPointsOnHand(card.getCardValue());
		}
		return checkIfBust(person);
	}

	private void placeBet(double bet) {
		if (bet <= 0 || Money.compare(player.getBalance(), bet) > 0) {
			throw new IllegalArgumentException("bet muss > 0 sein oder kleiner als player geld");
		}
		player.setBetAmount(Money.toMoney(bet));
		player.removeFromBalance(Money.toMoney(bet));
	}

	private void checkForAce(Person person) {
		if (person.getPointsOnHand() >= 11 && deck.viewCard().getCardColour().equals("A")) {
			// wechselt von 11 auf 1 Punkt
			deck.viewCard().switchAce();
		}
		if (person instanceof Player && ((Player) person).isHand2() && ((Player) person).getSplitPlayerPoints() >= 11 && deck.viewCard().getCardColour().equals("A")) {
			// wechselt von 11 auf 1 Punkt
			deck.viewCard().switchAce();
		}
	}

	private overdrawn checkIfBust(Person person) {
		if (person instanceof Player && ((Player) person).isHand2()) {
			if (((Player) person).getSplitPlayerPoints() > 21) {
				((Player) person).setBust2(true);
				return overdrawn.HAND2JA;
			}
			return overdrawn.NEIN;

		} else if (person.getPointsOnHand() > 21) {
			person.setBust(true);
			if (person instanceof Player && ((Player) person).isSplit()) {
				((Player) person).setHand2(true);
			}
			return overdrawn.JA;
		}
		return overdrawn.NEIN;
	}

	private roundType evaluateStart() {

		if (player.getHand().get(0).getCardValue() == player.getHand().get(1).getCardValue() && Money.compare(player.getBetAmount(), player.getBalance()) >= 0) {

			return roundType.SPLIT;
		} else if (player.getPointsOnHand() >= 9 && player.getPointsOnHand() <= 11 && Money.compare(player.getBetAmount(), player.getBalance()) >= 0) {
			return roundType.DOPPEL;
		} else if (player.getPointsOnHand() == 21) {
			return roundType.BLACKJACK;
		} else {
			return roundType.NORMAL;
		}
	}

	private String evaluateStopNormal() {
		switch (evaluateWinSituation()) {
		// Nachricht für Sieg des Croupiers
		case COUPIERGEWINNT:
			return "Der Croupier hat gewonnen. Sie verlieren Ihren Einsatz.";
		// Nachricht für Sieg des Spieler
		case SPIELERGEWINNT:
			player.addToBalance(Money.multiply(player.getBetAmount(), 2));
			return "Sie gewinnen ! Sie bekommen doppelten Einsatz zurück.";
		// Nachricht für Sieg beider Hände
		case UNENTSCHIEDEN:
			player.addToBalance(player.getBetAmount());
			return "Unentschieden. Geld zurück";
		default:
			return "ERROR";
		}
	}

	private String evaluateStopSplit() {
		switch (evaluateWinSituationSplit()) {
		// Nachricht für Sieg beider Hände
		case H1H2:
			player.addToBalance(Money.multiply(player.getBetAmount(), 2));
			return "Sie gewinnen Ihren Split mit beiden Händen.";
		// Nachricht für Sieg Hand 1
		case H1:
			player.addToBalance(player.getBetAmount());
			return "Sie gewinnen mit Hand 1, verlieren mit Hand2.";
		// Nachricht für Sieg Hand 2
		case H2:
			player.addToBalance(player.getBetAmount());
			return "Sie gewinnen mit Hand 2, verlieren mit Hand 1.";
		// Nachricht für Sieg des Croupiers
		case H0:
			return "Sie verlieren mit beiden Händen.";
		default:
			return "ERROR";
		}
	}

	private winSituation evaluateWinSituation() {
		// Hat der Croupier mehr Punkte als der Spieler gewinnt der Croupier
		if (player.isBust() || player.getPointsOnHand() < croupier.getPointsOnHand() && !croupier.isBust()) {
			return winSituation.COUPIERGEWINNT;
		} else if (player.getPointsOnHand() == croupier.getPointsOnHand() && !croupier.isBust()) {
			return winSituation.UNENTSCHIEDEN;
		} else {
			// Ansonsten gewinnt der Spieler
			return winSituation.SPIELERGEWINNT;
		}
	}

	private winSituation evaluateWinSituationSplit() {
		int spH1 = player.getPointsOnHand();
		int spH2 = player.getSplitPlayerPoints();
		int cp = croupier.getPointsOnHand();
		//Hand 1 und 2 gewinnen
		if (croupier.isBust() && !player.isBust() && !player.isBust2() || !player.isBust() && !player.isBust2() && spH1 > cp && spH2 > cp) {
			return winSituation.H1H2;
			// Situationen in denen nur Hand 2 gewinnt
		} else if (player.isBust() && !player.isBust2() && spH2 > cp || !player.isBust() && !player.isBust2() && spH1 <= cp && spH2 > cp) {
			return winSituation.H2;
			// Situationen in denen nur Hand 1 gewinnt
		} else if (player.isBust2() && !player.isBust() && spH1 > cp || !player.isBust() && !player.isBust2() && spH2 <= cp && spH1 > cp) {
			return winSituation.H1;
		} else {
			// sonst gewinnt keine Hand, Croupier gewinnt
			return winSituation.H0;
		}
	}

	private void reset() {
		player.delHand();
		player.delSplitPlayerHand();
		croupier.delHand();
		player.setPointsOnHand(0);
		player.setSplitPlayerPoints(0);
		croupier.setPointsOnHand(0);
		player.setBetAmount(Money.toMoney(0));
		player.setBust(false);
		player.setBust2(false);
		player.setSplit(false);
		player.setHand2(false);
		croupier.setBust(false);
	}

	public void takeMoneyFromBank(double plusBalance) {
		if (plusBalance <= 0 || Money.compare(player.getPlayerBank(), plusBalance) > 0) {
			throw new IllegalArgumentException("falscher Betrag");
		}
		player.setBalance(Money.add(player.getBalance(), plusBalance));
		player.setPlayerBank(Money.subtract(player.getPlayerBank(), plusBalance));
	}

	public boolean changePassword(String password) {
		String[] newSaltItarationsPassword = null;
		String newHashedPassword;

		try {
			newHashedPassword = Password.generateStrongPasswordHash(password);
			newSaltItarationsPassword = newHashedPassword.split(":");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.log(Level.WARNING, "Konnte kein neues Passwort erstellen", e);
			return false;
		}
		if (newSaltItarationsPassword != null) {
			logger.log(Level.INFO, "Passwort wurde geändert");
			player.setIterations(Integer.parseInt(newSaltItarationsPassword[0]));
			player.setSalt(newSaltItarationsPassword[1]);
			player.setPassword(newSaltItarationsPassword[2]);
			return true;

		}
		return false;
	}

	public void giftMoney() {
		Timestamp newTime = new Timestamp(System.currentTimeMillis());
		player.setPlayerBank(Money.add(player.getPlayerBank(), 1000));
		player.setTimeUntilMoneyIsAvailable(newTime);
		HibernateDatabaseActions.updateTimeStamp(newTime, player.getUsername());
	}

	public void startGame(double startMoney) {
		if (startMoney <= 0 || Money.compare(player.getPlayerBank(), startMoney) > 0) {
			throw new IllegalArgumentException("<=0 oder >playerBank");
		}
		player.setBalance(Money.toMoney(startMoney));
		player.setPlayerBank(Money.subtract(player.getPlayerBank(), startMoney));
		player.setLoggedIn(true);
	}

	public void exitGame() {
		player.setPlayerBank(Money.add(player.getPlayerBank(), player.getBalance()));
		HibernateDatabaseActions.saveUser(player);
	}

}
