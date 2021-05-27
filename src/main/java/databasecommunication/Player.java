package databasecommunication;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import blackjackobjects.Card;
import blackjackobjects.Money;
import blackjackobjects.MoneyConverter;
import blackjackobjects.Person;

@Entity
@Table(name = "BLACKJACKBENUTZER")
public class Player extends Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USERID")
	private int userID = 1;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "SALT")
	private String salt;
	@Column(name = "ITERATIONS")
	private int iterations;
	@Column(name = "DATUM")
	private Timestamp timeUntilMoneyIsAvailable;
	@Column(name = "MONEY")
	@Convert(converter = MoneyConverter.class)
	private Money playerBank;

	@Transient
	private Money betAmount;
	@Transient
	private Money balance;
	@Transient
	private int splitPlayerPoints;
	@Transient
	private List<Card> splitHand = new ArrayList<>();
	@Transient
	private boolean split = false;
	@Transient
	private boolean hand2 = false;
	@Transient
	private boolean ueberzogen2 = false;
	@Transient
	private boolean loggedIn = false;

	public Player() {
		// Kann leer sein
	}

	public void setSplitPlayerPoints(int anzahl) {
		this.splitPlayerPoints = anzahl;
	}

	public int getSplitPlayerPoints() {
		return this.splitPlayerPoints;
	}

	public void addSplitPlayerPoints(int anzahl) {
		this.splitPlayerPoints += anzahl;
	}

	public void addToSplitPlayerHand(Card card) {
		this.splitHand.add(card);
	}

	public List<Card> getSplitPlayerHand() {
		return this.splitHand;
	}

	public void delSplitPlayerHand() {
		this.splitHand.clear();
	}

	public Card getLastSplitPlayerCard() {
		return this.splitHand.get(this.splitHand.size() - 1);
	}

	public void addToBalance(Money geldDazu) {
		this.balance = Money.add(balance, geldDazu);
	}

	public void removeFromBalance(Money geldWeg) {
		this.balance = Money.subtract(balance, geldWeg);
	}

	public void setBalance(Money geld) {
		this.balance = geld;
	}

	public Money getBalance() {
		if (balance == null) {
			return Money.toMoney(0);
		}
		return this.balance;
	}

	public void setBetAmount(Money geld) {
		this.betAmount = geld;
	}

	public Money getBetAmount() {
		return this.betAmount;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserID() {
		return this.userID;
	}

	public void setPlayerBank(Money money) {
		this.playerBank = money;
	}

	public Money getPlayerBank() {
		return this.playerBank;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setTimeUntilMoneyIsAvailable(Timestamp time) {
		this.timeUntilMoneyIsAvailable = time;
	}

	public Timestamp getTimeUntilMoneyIsAvailable() {
		return this.timeUntilMoneyIsAvailable;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public boolean isHand2() {
		return hand2;
	}

	public void setHand2(boolean hand2) {
		this.hand2 = hand2;
	}

	public boolean isBust2() {
		return ueberzogen2;
	}

	public void setBust2(boolean bust2) {
		this.ueberzogen2 = bust2;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
