package controller;

import blackjackobjects.Card;
import blackjackobjects.Money;
import blackjackobjects.Person;
import model.listeners.*;
import databasecommunication.HibernateDatabaseActions;
import databasecommunication.Player;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.Timer;
import model.handlers.CardHandler;
import model.handlers.EvaluationHandler;
import model.handlers.MoneyHandler;
import view.GameGUI;

public class GameController {

    private final Logger logger = Logger.getLogger(GameController.class.getName());

    private final EvaluationHandler evaluationHandler;
    private final MoneyHandler moneyHandler;
    private final CardHandler cardHandler;
    private final Person croupier;
    private final Player player;
    private final GameGUI gameView;

    private boolean stateSplit;
    private boolean stateDouble;
    private boolean blackjack;

    public GameController(Player player) {
        this.player = player;
        this.croupier = new Person();
        this.moneyHandler = new MoneyHandler();
        this.evaluationHandler = new EvaluationHandler();
        this.cardHandler = new CardHandler();
        this.gameView = new GameGUI(player);

        this.addListener();
    }

    public void showView() {
        // Mach die SpielGUI sichtbar
        gameView.setVisible(true);
    }

    private void addListener() {
        // Fügt den Buttons der GUI actionListeners hinzu
        this.gameView.setStartActionListener(new StartActionListener(this));
        this.gameView.setProfileAddBalanceTFListener(new DefaultFocusListener(), new TakeMoneyFromBankActionListener(this));
        this.gameView.setChangePasswordActionListener(new ChangePasswordActionListener(this));
        this.gameView.setStopActionListener(new StopActionListener(this));
        this.gameView.setSplitActionListener(new SplitActionListener(this));
        this.gameView.setDoubleActionListener(new DoubleActionListener(this));
        this.gameView.setNewCardActionListener(new NewCardActionListener(this));
        this.gameView.setProfileAddUserMoneyActionListener(new AddMoneyActionListener(this));
        this.gameView.setProfileStartGameListener(new DefaultFocusListener(), new ProfileGameStartActionListener(this));
        this.gameView.setMenuExitActionListener(new MenuExitListener());
        this.gameView.setMenuHelpActionListener(new MenuHelpListener());
        this.gameView.setMenuProfileActionListener(new MenuProfileListener());
        this.gameView.setMenuGameActionListener(new MenuGameListener());
    }

    public void drawCroupierCards(String msg) {
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

        Timer buttonTimer = new Timer(delay, timere -> {
            getGameView().setPlayerBalance(getPlayer().getBalance());
            switchButtonsToStartMode();
            getGameView().updateStatus(msg);
        });
        buttonTimer.setRepeats(false);
        buttonTimer.start();
    }

    public void switchButtonsToStartMode() {
        gameView.switchDoubleButton(false);
        gameView.switchNewCardButton(false);
        gameView.switchSplitButton(false);
        gameView.switchStartButton(true);
        gameView.switchPlacedBet(true);
        gameView.switchStopButton(false);
    }

    private class MenuExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            player.setPlayerBank(Money.add(player.getPlayerBank(), player.getBalance()));
            HibernateDatabaseActions.saveUser(player);
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

    //GETTER AND SETTER

    public CardHandler getCardHandler() {
        return cardHandler;
    }

    public EvaluationHandler getEvaluationHandler() {
        return evaluationHandler;
    }

    public MoneyHandler getMoneyHandler() {
        return moneyHandler;
    }

    public Person getCroupier() {
        return croupier;
    }

    public Player getPlayer() {
        return player;
    }

    public GameGUI getGameView() {
        return gameView;
    }

    public boolean isStateSplit() {
        return stateSplit;
    }

    public void setStateSplit(final boolean stateSplit) {
        this.stateSplit = stateSplit;
    }

    public boolean isStateDouble() {
        return stateDouble;
    }

    public void setStateDouble(final boolean stateDouble) {
        this.stateDouble = stateDouble;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public void setBlackjack(final boolean blackjack) {
        this.blackjack = blackjack;
    }
}
