package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import blackjackobjects.Card;
import blackjackobjects.Person;
import blackjackobjects.Money;
import databasecommunication.Player;
import model.BlackJackGame;
import view.GameGUI;

public class GameController {
	private final Logger logger = Logger.getLogger(GameController.class.getName());

	private BlackJackGame game;
	private Person croupier;
	private GameGUI gameView;
	private Player player;

	private boolean stateS;
	private boolean stateD;
	private boolean blackjack;

	public GameController(Player player) {
		this.player = player;
		this.croupier = new Person();
		this.game = new BlackJackGame(player, croupier);
		this.gameView = new GameGUI(player);

		this.addListener();
	}

	public void showView() {
		// Mach die SpielGUI sichtbar
		gameView.setVisible(true);
	}

	private void addListener() {
		// Fügt den Buttons der GUI actionListeners hinzu
		this.gameView.setStartActionListener(new StartActionListener());
		this.gameView.setProfileAddBalanceTFListener(new DefaultFocusListener(), new TakeMoneyFromBankActionListener());
		this.gameView.setChangePasswordActionListener(new ChangePasswordActionListener());
		this.gameView.setStopActionListener(new StopActionListener());
		this.gameView.setSplitActionListener(new SplitActionListener());
		this.gameView.setDoubleActionListener(new DoubleActionListener());
		this.gameView.setNewCardActionListener(new NewCardActionListener());
		this.gameView.setProfileAddUserMoneyActionListener(new AddMoneyActionListener());
		this.gameView.setProfileStartGameListener(new DefaultFocusListener(), new ProfileGameStartActionListener());
		this.gameView.setMenuExitActionListener(new MenuExitListener());
		this.gameView.setMenuHelpActionListener(new MenuHelpListener());
		this.gameView.setMenuProfileActionListener(new MenuProfileListener());
		this.gameView.setMenuGameActionListener(new MenuGameListener());
	}

	private int placeCroupierCards() {
		int delay = 0;
		// Reset Punkte um Punkte je nach gezeigter Karte auszugeben
		croupier.setPointsOnHand(0);
		// for-schleife für Kartenausgabe
		for (Card croupierCard : croupier.getHand()) {

			if (croupierCard.equals(croupier.getHand().get(0))) {
				croupier.setPointsOnHand(croupier.getPointsOnHand() + croupierCard.getCardValue());
				gameView.setCroupierPoints(croupier.getPointsOnHand());
				continue;
			}
			// Timer für verzögerte Kartenausgabe
			Timer cardsTimer = new Timer(delay, timerkartene -> {
				// Hinzufügen der Karte und der Punkte
				gameView.addCroupierCard(croupierCard.getCardImage());

				croupier.setPointsOnHand(croupier.getPointsOnHand() + croupierCard.getCardValue());
				gameView.setCroupierPoints(croupier.getPointsOnHand());
				gameView.revalidate();
				gameView.repaint();
			});

			cardsTimer.setRepeats(false);
			cardsTimer.start();
			// Delay aufzählen für Verzögerung
			delay += 500;
		}
		return delay;
	}

	private void switchButtonsToStartMode() {
		gameView.switchDoubleButton(false);
		gameView.switchNewCardButton(false);
		gameView.switchSplitButton(false);
		gameView.switchStartButton(true);
		gameView.switchPlacedBet(true);
		gameView.switchStopButton(false);
	}

	private class StartActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.reset();
			try {

				double betAmount = gameView.getPlacedBetTF();

				// Auswertung welches Spiel gespielt wird, anhand des gelieferten enums

				switch (game.startRound(betAmount)) {
				case SPLIT:
					// Wenn Split, dann SplitButton aktiv und DoppelButton inaktiv
					stateS = true;
					stateD = false;
					break;
				case DOPPEL:
					// Wenn Doppel, dann SplitButton inaktiv und DoppelButtonAktiv
					stateS = false;
					stateD = true;
					break;
				case BLACKJACK:
					// Wenn BlackJack Split und DoppelButton inaktiv und BlackJack aktiv
					blackjack = true;
					break;
				case NORMAL:
					// Wenn normal Split und DoppelButton inaktiv
					stateD = false;
					stateS = false;
					break;
				default:
					logger.log(Level.WARNING, "Fehler bei Spielsituationauswertung");
					break;
				}
				// Update die GUI mit C-Karte und S-Karten
				SwingUtilities.invokeLater(() -> {

					gameView.switchStartButton(false);
					gameView.setPlayerBetAmount(player.getBetAmount());
					gameView.setPlayerBalance(player.getBalance());
					gameView.addCroupierCard(croupier.getLastCardOnHand().getCardImage());
					gameView.setCroupierPoints(croupier.getPointsOnHand());
					gameView.revalidate();
					gameView.repaint();
					// Init delay für verzögerte Ausgabe
					int delay = 0;
					// Reset Punkte um Punkte je nach gezeigter Karte auszugeben
					player.setPointsOnHand(0);
					// for-schleife für Kartenausgabe
					for (Card playerCard : player.getHand()) {
						// Timer für verzögerte Kartenausgabe
						Timer cardsTimer = new Timer(delay, timerkartene -> {
							// Hinzufügen der Karte und der Punkte
							gameView.addPlayerCard(playerCard.getCardImage());
							gameView.switchPlacedBet(false);
							player.setPointsOnHand(player.getPointsOnHand() + playerCard.getCardValue());
							gameView.setPlayerPoints(player.getPointsOnHand());
							gameView.revalidate();
							gameView.repaint();
						});

						cardsTimer.setRepeats(false);
						cardsTimer.start();
						// Delay aufzählen für Verzögerung
						delay += 500;
					}
					// Nach Ausgabe der Karten jeweilige Buttons aktivieren
					Timer buttonTimer = new Timer(delay, timere -> {
						if (blackjack) {
							String msg = game.blackjack();
							gameView.addCroupierCard(croupier.getLastCardOnHand().getCardImage());
							gameView.setCroupierPoints(croupier.getPointsOnHand());
							gameView.setPlayerBalance(player.getBalance());
							switchButtonsToStartMode();
							gameView.updateStatus(msg);
							blackjack = false;
						} else {
							gameView.switchSplitButton(stateS);
							gameView.switchDoubleButton(stateD);
							gameView.switchStopButton(true);
							gameView.switchNewCardButton(true);
						}
					});
					buttonTimer.setRepeats(false);
					buttonTimer.start();
				});
			} catch (IllegalArgumentException i) {
				SwingUtilities.invokeLater(() -> gameView.updateStatus("Richtigen betrag eingeben"));
			}
		}
	}

	private class NewCardActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (game.givePlayingCard(player)) {
			case NEIN:
				SwingUtilities.invokeLater(() -> {
					gameView.switchDoubleButton(false);
					gameView.switchSplitButton(false);
					if (player.isHand2()) {
						gameView.addSplitCard(player.getLastSplitPlayerCard().getCardImage());
						gameView.setSplitPlayerPoints(player.getSplitPlayerPoints());
					} else {
						gameView.addPlayerCard(player.getLastCardOnHand().getCardImage());
						gameView.setPlayerPoints(player.getPointsOnHand());
					}
				});
				break;
			case JA:
				SwingUtilities.invokeLater(() -> {
					if (!player.isSplit()) {
						gameView.addPlayerCard(player.getLastCardOnHand().getCardImage());
						gameView.setPlayerPoints(player.getPointsOnHand());
						gameView.setPlayerBalance(player.getBalance());
						switchButtonsToStartMode();
						gameView.updateStatus("Sie haben überzogen.");
					} else {
						gameView.addPlayerCard(player.getLastCardOnHand().getCardImage());
						gameView.setPlayerPoints(player.getPointsOnHand());
						gameView.updateStatus("Hand 2 wird gespielt");
					}
				});
				break;
			case HAND2JA:
				String msg = game.stopRound();
				SwingUtilities.invokeLater(() -> {
					gameView.addSplitCard(player.getLastSplitPlayerCard().getCardImage());
					gameView.setSplitPlayerPoints(player.getSplitPlayerPoints());
					int delay = placeCroupierCards();
					// Nach Ausgabe der Karten jeweilige Buttons aktivieren
					Timer buttonTimer = new Timer(delay, timere -> {
						gameView.setPlayerBalance(player.getBalance());
						switchButtonsToStartMode();
						gameView.updateStatus(msg);
					});
					buttonTimer.setRepeats(false);
					buttonTimer.start();

				});
				break;
			default:
				break;
			}

		}
	}

	private class StopActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = game.stopRound();
			if (msg.equals("Hand 2 wird gespielt")) {
				SwingUtilities.invokeLater(() -> gameView.updateStatus(msg));
			} else {
				SwingUtilities.invokeLater(() -> {
					int delay = placeCroupierCards();
					// Nach Ausgabe der Karten jeweilige Buttons aktivieren
					Timer buttonTimer = new Timer(delay, timere -> {
						gameView.setPlayerBalance(player.getBalance());
						switchButtonsToStartMode();
						gameView.updateStatus(msg);
					});
					buttonTimer.setRepeats(false);
					buttonTimer.start();

				});
			}
		}
	}

	private class SplitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// Spieler Einstellungen für Split werden getroffen
			game.splitRound();
			// GUI wird für Split bereitgemacht
			SwingUtilities.invokeLater(() -> {
				gameView.switchShowSplitPlayer(true);
				gameView.switchSplitButton(false);
				gameView.removeSplitPlayerCard(player.getLastSplitPlayerCard().getCardImage());
				gameView.setPlayerBalance(player.getBalance());
				gameView.setPlayerBetAmount(Money.divide(player.getBetAmount(), 2));
				gameView.setPlayerPoints(player.getLastCardOnHand().getCardValue());
				gameView.setSplitPlayerPoints(player.getLastSplitPlayerCard().getCardValue());
				gameView.setSplitPlayerBetAmount(Money.divide(player.getBetAmount(), 2));
				gameView.updateStatus("Hand 1 wird nun gespielt");
			});
		}
	}

	private class DoubleActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = game.doubleRound();
			SwingUtilities.invokeLater(() -> {
				gameView.setPlayerBetAmount(player.getBetAmount());
				gameView.setPlayerBalance(player.getBalance());
				gameView.addPlayerCard(player.getLastCardOnHand().getCardImage());
				gameView.setPlayerPoints(player.getPointsOnHand());
				int delay = placeCroupierCards();
				// Nach Ausgabe der Karten jeweilige Buttons aktivieren
				Timer buttonTimer = new Timer(delay, timere -> {
					gameView.setPlayerBalance(player.getBalance());
					switchButtonsToStartMode();
					gameView.updateStatus(msg);
				});
				buttonTimer.setRepeats(false);
				buttonTimer.start();

			});
		}
	}

	private class TakeMoneyFromBankActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			try {
				game.takeMoneyFromBank(gameView.getProfileAddBalanceTF());
				SwingUtilities.invokeLater(() -> {
					gameView.setProfileBalance(player.getPlayerBank().toString());
					gameView.setPlayerBalance(gameView.getPlayer().getBalance());
					gameView.updateProfileStatus("Guthaben aufgeladen", true);
				});
			} catch (IllegalArgumentException e2) {
				SwingUtilities.invokeLater(() -> gameView.updateProfileStatus("Bitte richtigen Betrag eingeben", false));
			}
		}
	}

	private class ChangePasswordActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			if (game.changePassword(gameView.getPassword())) {
				SwingUtilities.invokeLater(() -> {
					gameView.updateProfileStatus("Ihr Passwort wurde erfolgreich geändert", true);
					gameView.setProfileChangePasswordTF("");
				});

			} else {
				SwingUtilities.invokeLater(() -> gameView.updateProfileStatus("Fehler beim ändern des Passwortes", false));
			}
		}
	}

	private class AddMoneyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			game.giftMoney();
			SwingUtilities.invokeLater(() -> {
				gameView.setProfileBalance(player.getPlayerBank().toString());
				gameView.updateProfileStatus("Wir schenken Ihnen 1000€.", true);

			});
		}
	}

	private class ProfileGameStartActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				game.startGame(gameView.getProfileStartGameTF());
				SwingUtilities.invokeLater(() -> {
					gameView.setPlayerBalance(Money.toMoney(gameView.getProfileStartGameTF()));
					gameView.setProfileBalance(gameView.getPlayer().getPlayerBank().toString());
					gameView.addUserInfoPanel();
					gameView.updateProfileStatus("", false);
					gameView.updateStatus("Sie Können jetzt Spielen!");
					gameView.switchToGame();
				});
			} catch (IllegalArgumentException e2) {
				SwingUtilities.invokeLater(() -> gameView.updateProfileStatus("Bitte geben sie einen Richtigen Betrag ein!", false));
			}
		}
	}

	private class MenuExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			game.exitGame();
			gameView.dispose();
			System.exit(1);
		}

	}

	private class MenuProfileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.switchToProfile();
		}

	}

	private class MenuHelpListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.switchToRules();
		}

	}

	private class MenuGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.switchToGame();
		}

	}

	private class DefaultFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			((JTextField) e.getSource()).setText("");
			((JTextField) e.getSource()).setForeground(Color.BLACK);
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (((JTextField) e.getSource()).getText().equals("")) {
				((JTextField) e.getSource()).setText(((JTextField) e.getSource()).getToolTipText());
				((JTextField) e.getSource()).setForeground(Color.GRAY);

			}
		}
	}
}
