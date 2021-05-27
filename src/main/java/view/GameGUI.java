package view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import blackjackobjects.Money;
import databasecommunication.Player;
import model.GUIFactory;
import model.JLabelFactory;
import model.PasswordFieldBuilder;

@SuppressWarnings("serial")
public class GameGUI extends JFrame {

	private JPanel gamePanel;
	private JPanel cardPanel;
	private JPanel croupierPanel;
	private JPanel playerPanel;
	private JPanel splitPanel;
	private JPanel croupierCards;
	private JPanel playerCardsUpperPanel;
	private JPanel playerCardsPanel;
	private JPanel splitPlayerCardsPanel;
	private JPanel userInfoPanel;

	private JButton stopButton;
	private JButton splitButton;
	private JButton doubleButton;
	private JButton newCardButton;
	private JButton startButton;
	private JButton profileChangePasswordButton;
	private JButton profileAddBetMoneyButton;
	private JButton profileStartGameButton;
	private JButton profileAddUserMoneyButton;

	private JTextField placedBet;
	private JTextField profileStartGameTextField;
	private JTextField profileaddBalanceTextField;
	private JPasswordField profileChangePasswordPWF;

	private JLabel splitPlayerPoints;
	private JLabel splitPlayerBetAmount;
	private JLabel croupierPoints;
	private JLabel playerPoints;
	private JLabel status;
	private JLabel playerBalance;
	private JLabel playerBetAmount;
	private JLabel profileStatus;
	private JLabel profileBalance;
	private JLabel profileTimeUntilMoneyIsAvailable;
	private JLabel profileUsername;
	private JLabel profileUsername2;

	private JMenuItem menuExit;
	private JMenuItem menuHelp;
	private JMenuItem menuProfile;
	private JMenuItem menuGame;

	private transient Player normalPlayer;

	public GameGUI(Player player) {

		// Setzt Das Grund Layout
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 1));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setTitle("CasinoBew - BlackJack");
		this.setUndecorated(true);

		this.setJMenuBar(blackJackMenuBar());
		this.normalPlayer = player;

		// CROUPIER PANEL
		croupierPanel = new JPanel(new GridLayout(1, 2));
		croupierCards = new JPanel(new GridLayout(1, 0));
		JPanel croupierLabels = new JPanel(new GridLayout(1, 3));
		croupierPoints = new JLabel();
		status = new JLabel();

		// SPIELER PANEL
		playerPanel = new JPanel(new GridLayout(1, 2));

		// KARTEN PANEL
		playerCardsUpperPanel = new JPanel(new GridLayout(0, 1));
		playerCardsPanel = new JPanel(new GridLayout(1, 0));
		splitPlayerCardsPanel = new JPanel(new GridLayout(1, 0));

		// SPLIT PANEL
		splitPanel = new JPanel(new GridLayout(1, 3));
		splitPlayerPoints = JLabelFactory.builder().text("Split1").setVisible(false).build();
		splitPlayerBetAmount = JLabelFactory.builder().text("Split2").setVisible(false).build();

		// BUTTON PANEL
		JPanel spielerButtons = new JPanel(new GridLayout(2, 1));

		// BUTTON PANEL UNTEN
		JPanel spielerButtonsUnten = new JPanel(new GridLayout(1, 4));
		stopButton = GUIFactory.newButton("Stop", false);
		newCardButton = GUIFactory.newButton("Neue Karte", false);
		doubleButton = GUIFactory.newButton("Doppeln", false);
		splitButton = GUIFactory.newButton("Split", false);

		// BUTTON PANEL OBEN
		JPanel spielerButtonsOben = new JPanel(new GridLayout(0, 5));
		playerPoints = new JLabel();
		playerBetAmount = JLabelFactory.builder().setEnabled(false).build();
		playerBalance = new JLabel("Ihr Guthaben: " + player.getBalance());
		placedBet = GUIFactory.newTextField("Geld Setzen", null, "");
		startButton = GUIFactory.newButton("Start", true);

		spielerButtonsUnten.add(stopButton);
		spielerButtonsUnten.add(newCardButton);
		spielerButtonsUnten.add(doubleButton);
		spielerButtonsUnten.add(splitButton);

		splitPanel.add(splitPlayerPoints);
		splitPanel.add(splitPlayerBetAmount);

		spielerButtonsOben.add(playerPoints);
		spielerButtonsOben.add(playerBetAmount);
		spielerButtonsOben.add(playerBalance);
		spielerButtonsOben.add(placedBet);
		spielerButtonsOben.add(startButton);

		spielerButtonsOben.add(splitPanel);

		spielerButtons.add(spielerButtonsOben);
		spielerButtons.add(spielerButtonsUnten);

		playerCardsUpperPanel.add(playerCardsPanel);
		playerCardsUpperPanel.add(splitPlayerCardsPanel);

		// ZUSAMMENSETZEN SPIELER PANEL
		playerPanel.add(playerCardsUpperPanel);
		playerPanel.add(spielerButtons);

		// ZUSAMMENSETZEN CROUPIER PANEL
		croupierLabels.add(croupierPoints);
		croupierLabels.add(status);
		croupierPanel.add(croupierCards);
		croupierPanel.add(croupierLabels);

		// ZUSAMMENSETZEN GAME PANEL
		gamePanel = new JPanel(new GridLayout(2, 1));
		gamePanel.add(croupierPanel);
		gamePanel.add(playerPanel);

		// PANEL ANZEIGE WECHSELN
		cardPanel = new JPanel(new CardLayout());

		zeitFuerGeld.execute();
		cardPanel.add(profilePane(), "KONTO");
		cardPanel.add(gamePanel, "SPIEL");
		cardPanel.add(helpPane(), "HILFE");
		getContentPane().add(cardPanel);

		// Skaliert frame und macht es sichtbar
		this.pack();
		this.setVisible(true);
	}

	public void setStopActionListener(ActionListener stopButtonEVT) {
		stopButton.addActionListener(stopButtonEVT);
	}

	public void setNewCardActionListener(ActionListener newCardButtonEVT) {
		newCardButton.addActionListener(newCardButtonEVT);
	}

	public void setDoubleActionListener(ActionListener doubleButtonEVT) {
		doubleButton.addActionListener(doubleButtonEVT);
	}

	public void setSplitActionListener(ActionListener splitButtonActionListener) {
		splitButton.addActionListener(splitButtonActionListener);
	}

	public void setStartActionListener(ActionListener startButtonListener) {
		this.startButton.addActionListener(startButtonListener);
		this.placedBet.addActionListener(startButtonListener);

	}

	public void addCroupierCard(ImageIcon icon) {
		this.croupierCards.add(new JLabel(icon));
	}

	public void addPlayerCard(ImageIcon icon) {
		this.playerCardsPanel.add(new JLabel(icon));
	}

	public void addSplitCard(ImageIcon icon) {
		this.splitPlayerCardsPanel.add(new JLabel(icon));
	}

	public void switchStopButton(boolean state) {
		this.stopButton.setEnabled(state);
	}

	public void switchSplitButton(boolean state) {
		this.splitButton.setEnabled(state);
	}

	public void switchStartButton(boolean state) {
		this.startButton.setEnabled(state);
	}

	public void switchNewCardButton(boolean state) {
		this.newCardButton.setEnabled(state);
	}

	public void switchDoubleButton(boolean state) {
		this.doubleButton.setEnabled(state);
	}

	public void setCroupierPoints(int msg) {
		String text = Integer.toString(msg);
		this.croupierPoints.setText(text);
	}

	public void setPlayerPoints(int msg) {
		String text = Integer.toString(msg);
		this.playerPoints.setText(text);
	}

	public void setPlayerBetAmount(Money money) {
		String msg = "Gesetzter Wert: " + money.toString();
		this.playerBetAmount.setText(msg);
		this.playerBetAmount.setEnabled(true);
	}

	public void updateStatus(String msg) {
		this.status.setText(msg);
	}

	public void setProfileTimeUntilMoneyIsAvailable(String msg) {
		this.profileTimeUntilMoneyIsAvailable.setText(msg);
	}

	public void setPlayerBalance(Money money) {
		this.playerBalance.setText("Ihr Guthaben: " + money);
	}

	public void removeSplitPlayerCard(ImageIcon icon) {
		this.splitPlayerCardsPanel.add(new JLabel(icon));
		this.playerCardsPanel.remove(1);

	}

	public void setSplitPlayerPoints(int msg) {
		String text = Integer.toString(msg);
		this.splitPlayerPoints.setText(text);
	}

	public void switchShowSplitPlayer(boolean state) {
		splitPlayerPoints.setVisible(state);
		splitPlayerBetAmount.setVisible(state);
		splitPanel.setVisible(state);
		splitPlayerCardsPanel.setVisible(state);
	}

	public void switchPlacedBet(boolean state) {
		this.placedBet.setEnabled(state);
	}

	public void setSplitPlayerBetAmount(Money money) {
		this.splitPlayerBetAmount.setText(money.toString());
	}

	public double getPlacedBetTF() {
		String text = this.placedBet.getText();
		if (text.contains(",")) {
			text = text.replace(",", ".");
		}
		return Double.parseDouble(text);
	}

	public String getPassword() {
		return new String(this.profileChangePasswordPWF.getPassword());
	}

	public Player getPlayer() {
		return this.normalPlayer;
	}

	public double getProfileAddBalanceTF() {
		String text = this.profileaddBalanceTextField.getText();
		if(text.contains(",")) {
			text = text.replace(",", ".");
		}
		return Double.parseDouble(text);
	}

	// SPIELGUI ENDE

	// MENUBAR PANE

	private JMenuBar blackJackMenuBar() {
		JMenuBar bar;
		JMenu file;

		bar = new JMenuBar();
		bar.setBorderPainted(true);

		file = new JMenu("Aktionen");

		menuExit = GUIFactory.newMenuItem("Spiel beenden");
		file.add(menuExit);

		menuHelp = GUIFactory.newMenuItem("Regeln");
		file.add(menuHelp);

		menuProfile = GUIFactory.newMenuItem("Konto");
		file.add(menuProfile);

		menuGame = GUIFactory.newMenuItem("Spiel");
		file.add(menuGame);

		bar.add(file);
		return bar;
	}

	public void switchToRules() {
		CardLayout cl = (CardLayout) (this.cardPanel.getLayout());
		cl.show(this.cardPanel, "HILFE");
	}

	public void switchToProfile() {
		CardLayout cl = (CardLayout) (this.cardPanel.getLayout());
		cl.show(this.cardPanel, "KONTO");
	}

	public void switchToGame() {
		CardLayout cl = (CardLayout) (this.cardPanel.getLayout());
		cl.show(this.cardPanel, "SPIEL");
	}

	public void setMenuExitActionListener(ActionListener al) {
		this.menuExit.addActionListener(al);
	}

	public void setMenuHelpActionListener(ActionListener al) {
		this.menuHelp.addActionListener(al);
	}

	public void setMenuProfileActionListener(ActionListener al) {
		this.menuProfile.addActionListener(al);
	}

	public void setMenuGameActionListener(ActionListener al) {
		this.menuGame.addActionListener(al);
	}

	// MENUBAR ENDE

	// HILFE PANE

	private JPanel helpPane() {

		final Font headlines = new Font("Helvetica", Font.PLAIN, 26);
		final Font text = new Font("TimesRoman", Font.PLAIN, 18);
		JPanel helpPanel;

		// Panel erstellen
		helpPanel = new JPanel(new GridLayout(0, 1));
		helpPanel.setBackground(Color.white);

		// Array mit allen Überschriften
		JLabel[] rulesHeadlines = { new JLabel("Ablauf"), new JLabel("Hit/Stand"), new JLabel("Split"), new JLabel("Double/Doppel"), new JLabel("Push/Tie"), new JLabel("Bust"),
				new JLabel("BlackJack") };

		// Erstellt alle Überschrift Labels
		for (int i = 0; i < rulesHeadlines.length; i++) {
			rulesHeadlines[i].setHorizontalAlignment(SwingConstants.CENTER);
			rulesHeadlines[i].setFont(headlines);
		}

		// Array mit allen Text String
		JTextArea[] rulesText = { new JTextArea(
				"Das Ziel von BlackJack ist relativ einfach, das Ziel des Spiels ist es, mit zwei oder mehr Karten näher an 21 Punkte heranzukommen als der Dealer, ohne dabei die 21 Punkte zu überschreiten."),
				new JTextArea(
						"Hit und Stand sind die beiden grundlegenden Aktionen, welche ein Spieler, sobald er an der Reihe ist, wählen kann. Mit dem Befehl Hit fordert der Spieler eine weitere Karte an. Dieses Befehl wird auch gerne als Draw bezeichnet. Stand ist dagegen genau das Gegenteil. Mit dem Befehl Stand verneint man eine weitere Karte und lässt sich keine weitere Karten austeilen. Das aktive Spiel ist für den Stand-Spieler vorbei."),
				new JTextArea(
						"Ein sogenannter Split gehört ebenfalls zum Standardrepertoire eines BlackJack-Spielers. Sollte der Spieler in der ersten Runde des Ausgebens zweimal eine Karte mit dem gleichen Wert erhalten, so darf der Spieler diese aufsplitten und als zwei separate Hände spielen. Dies bedeutet, er bekommt zu beiden Karten jeweils eine neue Karte hinzu und hat somit zwei Hände vor sich zu liegen. Für die zusätzliche Hand muss der Spieler den gleichen Wetteinsatz noch einmal legen, den er schon für die erste Hand gesetzt hatte."),
				new JTextArea(
						"Der Doppel ist ein Spielzug, den man in einer recht aussichtsreichen Lage anwenden kann. Ein Doppel wird dann angesagt, wenn alle Spieler zwei Karten erhalten haben. Der Spieler bekommt das Recht, seinen Einsatz zu verdoppeln, kann und muss dafür jedoch nur noch eine Karte erhalten. Ein Doppeln wird meist dann gespielt, wenn der Wert der Kartenhand zwischen neun und elf liegt."),
				new JTextArea(
						"Push und Tie sind zwei Begriffe für die gleiche Spielsituation. Nun befinden wir uns nicht mehr bei den Spielaktionen, sondern bei den Situationen. Ein Push/Tie entsteht, wenn der Wert der Hände des Spielers und des Dealers identisch ist. Daraufhin erhält der Spieler den Wetteinsatz zurück."),
				new JTextArea(
						"Ein Bust ist für den Spieler das schlechteste Ergebnis. Man hat sich überwettet, da der Wert der eigenen Hand über 21 gestiegen ist. Man hat nun automatisch seinen Wetteinsatz verloren, unabhängig davon, ob sich der Dealer noch überkauft oder nicht. Ein zentraler Bestandteil des Spiels ist abzuschätzen, wie hoch das Risiko für den Bust bei einem Hit ist und ob man es je nach der ersten Karte des Dealers eingehen sollte."),
				new JTextArea(
						"Ein BlackJack ist die beste Hand, die es im Spiel BlackJack gibt. Sie besteht aus einem Ass und einer 10ner-Karte, also König, Dame, Bube oder 10. Ein BlackJack, der sich nur aus zwei Karten bilden kann, gewinnt die Runde automatisch.") };

		// Erstellt alle Text Labels
		for (int i = 0; i < rulesText.length; i++) {
			rulesText[i].setBackground(Color.white);
			rulesText[i].setWrapStyleWord(true);
			rulesText[i].setLineWrap(true);
			rulesText[i].setFont(text);
			rulesText[i].setEditable(false);
		}

		// Fuegt alles in der Richtigen Reihenfolge ein
		for (int i = 0; i < rulesHeadlines.length; i++) {
			helpPanel.add(rulesHeadlines[i]);
			helpPanel.add(rulesText[i]);
		}

		return helpPanel;
	}

	// HILE PANE ENDE

	// KONTO PANE

	private JPanel profilePane() {

		JPanel profile = new JPanel(new GridLayout(0, 1));

		userInfoPanel = new JPanel();
		userInfoPanel.setForeground(Color.BLACK);
		userInfoPanel.setLayout(new FormLayout(new ColumnSpec[] { //
				ColumnSpec.decode("700px"), //
				FormSpecs.RELATED_GAP_COLSPEC, //
				ColumnSpec.decode("200px"), //
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, //
				ColumnSpec.decode("150px"), //
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, //
				ColumnSpec.decode("250px"), //
				ColumnSpec.decode("90px"), }, //
				new RowSpec[] { //
						RowSpec.decode("100px"), //
						FormSpecs.DEFAULT_ROWSPEC, //
						FormSpecs.RELATED_GAP_ROWSPEC, //
						RowSpec.decode("15px"), //
						FormSpecs.RELATED_GAP_ROWSPEC, //
						FormSpecs.DEFAULT_ROWSPEC, //
						FormSpecs.RELATED_GAP_ROWSPEC, //
						FormSpecs.DEFAULT_ROWSPEC, //
						FormSpecs.RELATED_GAP_ROWSPEC, //
						FormSpecs.DEFAULT_ROWSPEC, //
						FormSpecs.RELATED_GAP_ROWSPEC, //
						FormSpecs.DEFAULT_ROWSPEC, //
						RowSpec.decode("85px"), }));

		// BENUTZERNAME
		profileUsername = new JLabel("Ihr Benutzername: ");
		profileUsername2 = new JLabel(normalPlayer.getUsername());

		// GUTHABEN
		JLabel profileCurrentBalance = new JLabel("Aktuelles Guthaben: ");
		profileBalance = new JLabel(normalPlayer.getPlayerBank().toString());

		// PW ÄNDERN
		profileChangePasswordPWF = PasswordFieldBuilder.builder().toolTipText("neues Passwort").build();
		profileChangePasswordButton = GUIFactory.newButton("Passwort Ändern", true);

		// GUTHABEN HINZUFÜGEN
		profileaddBalanceTextField = GUIFactory.newTextField("Guthaben hinzufügen", null, "");
		profileAddBetMoneyButton = GUIFactory.newButton("Guthaben Aufladen", true);

		// SPIEL START
		profileStartGameTextField = GUIFactory.newTextField("Guthaben abheben", null, "Mit wie viel Geld wollen Sie spielen?");
		profileStartGameButton = GUIFactory.newButton("Spiel Starten", true);
		profileStatus = new JLabel("Geben Sie bitte Ihr start Guthaben ein");

		// GELD HINZUFÜGEN ALLE 2 STUNDEN
		JPanel profileMoneyPanel = new JPanel(new FlowLayout());

		profileTimeUntilMoneyIsAvailable = new JLabel("'Es dauert noch xy min bis....");
		profileAddUserMoneyButton = GUIFactory.newButton("Geld hinzufügen", false);

		if (getPlayer().isLoggedIn()) {
			addUserInfoPanel();
		}
		userInfoPanel.add(profileCurrentBalance, "3, 6");
		userInfoPanel.add(profileBalance, "5, 6");
		userInfoPanel.add(profileStatus, "7, 13");
		userInfoPanel.add(profileStartGameTextField, "3, 13");
		userInfoPanel.add(profileStartGameButton, "5, 13"); // NULL -> PANE WECHSEL => SPIEL
		profileMoneyPanel.add(profileTimeUntilMoneyIsAvailable);
		profileMoneyPanel.add(profileAddUserMoneyButton);
		profile.add(userInfoPanel);
		profile.add(profileMoneyPanel);
		return profile;
	}

	public void addUserInfoPanel() {
		userInfoPanel.add(profileUsername, "3, 4, default, top");
		userInfoPanel.add(profileUsername2, "5, 4");
		userInfoPanel.add(profileChangePasswordPWF, "3, 8, fill, default");
		userInfoPanel.add(profileChangePasswordButton, "5, 8");
		userInfoPanel.add(profileaddBalanceTextField, "3, 12, fill, default");
		userInfoPanel.add(profileAddBetMoneyButton, "5, 12");
		removeUserStartInfo();
	}

	private void removeUserStartInfo() {
		userInfoPanel.remove(profileStartGameButton);
		userInfoPanel.remove(profileStartGameTextField);
	}

	public void setProfileBalance(String text) {
		this.profileBalance.setText(text);
	}

	public double getProfileStartGameTF() {
		String text = this.profileStartGameTextField.getText();
		if (text.contains(",")) {
			text = text.replace(",", ".");
		}
		return Double.parseDouble(text);
	}

	public void setProfileChangePasswordTF(String text) {
		this.profileChangePasswordPWF.setText(text);
	}

	public void setChangePasswordActionListener(ActionListener al) {
		profileChangePasswordButton.addActionListener(al);
	}

	public void setProfileAddBalanceTFListener(FocusListener a1, ActionListener a2) {
		profileaddBalanceTextField.addFocusListener(a1);
		profileAddBetMoneyButton.addActionListener(a2);
	}

	public void updateProfileStatus(String msg, boolean state) {
		if(state) {
			this.profileStatus.setForeground(Color.green);
		} else {
			this.profileStatus.setForeground(Color.red);
		}
		profileStatus.setText(msg);
	}

	public void setProfileStartGameListener(FocusListener a1, ActionListener a2) {
		profileStartGameTextField.addFocusListener(a1);
		profileStartGameButton.addActionListener(a2);
	}

	public void setProfileAddUserMoneyActionListener(ActionListener a1) {
		profileAddUserMoneyButton.addActionListener(a1);
	}

	// KONTO PANE ENDE

	transient SwingWorker<Void, Integer> zeitFuerGeld = new SwingWorker<Void, Integer>() {

		@Override
		protected Void doInBackground() throws Exception {

			ScheduledExecutorService execService = Executors.newScheduledThreadPool(1);
			execService.scheduleAtFixedRate(() -> {
				Timestamp oldTime = normalPlayer.getTimeUntilMoneyIsAvailable();
				Timestamp currentTime = new Timestamp(System.currentTimeMillis());
				long milliseconds = currentTime.getTime() - oldTime.getTime();
				int seconds = (int) milliseconds / 1000;
				int zeitBisGeld = 120 - (seconds / 60);
				if (zeitBisGeld < 0) {
					zeitBisGeld = 0;
				}
				publish(zeitBisGeld);
			}, 0, 1, TimeUnit.SECONDS);

			return null;
		}

		@Override
		// Can safely update the GUI from this method.
		protected void process(List<Integer> chunks) {
			int aktuelleZeitBis = chunks.get(chunks.size() - 1);
			if (aktuelleZeitBis == 0) {
				profileAddUserMoneyButton.setEnabled(true);
			} else {
				profileAddUserMoneyButton.setEnabled(false);
			}
			profileTimeUntilMoneyIsAvailable.setText("Es dauert noch " + aktuelleZeitBis + " Minuten, bis Sie Geld Hinzufügen können.");

		}

	};

	private void deletePreviousPanel(JPanel panel) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JLabel) {
				panel.remove(comp);
			}
		}
	}

	public void reset() {

		normalPlayer.setPointsOnHand(0);
		normalPlayer.setSplitPlayerPoints(0);
		normalPlayer.setBetAmount(Money.toMoney(0));
		status.setText("");
		deletePreviousPanel(splitPlayerCardsPanel);
		deletePreviousPanel(croupierCards);
		deletePreviousPanel(playerCardsPanel);
		splitPlayerPoints.setVisible(false);
		splitPlayerBetAmount.setVisible(false);
		splitPlayerCardsPanel.setVisible(false);
		playerBetAmount.setEnabled(false);
	}

}