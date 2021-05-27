package controller.listeners;

import blackjackobjects.Card;
import blackjackobjects.Money;
import blackjackobjects.Person;
import controller.GameController;
import databasecommunication.Player;
import model.CardHandler;
import view.GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartActionListener implements ActionListener {

    private final Logger logger = Logger.getLogger(GameController.class.getName());

    private final GameController gameController;

    public StartActionListener(final GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameGUI gameView = gameController.getGameView();
        CardHandler cardHandler = gameController.getCardHandler();
        Player player = gameController.getPlayer();
        Person croupier = gameController.getCroupier();

        gameView.reset();
        try {

            double betAmount = gameView.getPlacedBetTF();
            player.deleteHand();
            player.delSplitPlayerHand();
            croupier.deleteHand();
            player.setPointsOnHand(0);
            player.setSplitPlayerPoints(0);
            croupier.setPointsOnHand(0);
            player.setBetAmount(Money.toMoney(0));
            player.setBust(false);
            player.setBust2(false);
            player.setSplit(false);
            player.setHand2(false);
            croupier.setBust(false);
            gameController.getMoneyHandler().placeBet(player, betAmount);
            cardHandler.createNewDeck();
            cardHandler.givePlayingCard(croupier);
            cardHandler.givePlayingCard(player);
            cardHandler.givePlayingCard(player);
            // Auswertung welches Spiel gespielt wird, anhand des gelieferten enums
            switch (gameController.getEvaluationHandler().evaluateStart(player)) {
                case SPLIT:
                    // Wenn Split, dann SplitButton aktiv und DoppelButton inaktiv
                    gameController.setStateSplit(true);
                    gameController.setStateDouble(false);
                    break;
                case DOPPEL:
                    // Wenn Doppel, dann SplitButton inaktiv und DoppelButtonAktiv
                    gameController.setStateSplit(false);
                    gameController.setStateDouble(true);
                    break;
                case BLACKJACK:
                    // Wenn BlackJack Split und DoppelButton inaktiv und BlackJack aktiv
                    gameController.setBlackjack(true);
                    break;
                case NORMAL:
                    // Wenn normal Split und DoppelButton inaktiv
                    gameController.setStateDouble(false);
                    gameController.setStateSplit(false);
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
                    if (gameController.isBlackjack()) {
                        String msg;
                        cardHandler.givePlayingCard(croupier);
                        if (croupier.getPointsOnHand() == 21) {
                            msg = "Sie und Croupier haben einen BlackJack. Einsatz zurück.";
                        } else {
                            player.addToBalance(Money.multiply(player.getBetAmount(), 3));
                            msg = "Sie gewinnen mit einem BlackJack. 3-Fachen Einsatz zurück.";
                        }
                        gameView.addCroupierCard(croupier.getLastCardOnHand().getCardImage());
                        gameView.setCroupierPoints(croupier.getPointsOnHand());
                        gameView.setPlayerBalance(player.getBalance());
                        gameController.switchButtonsToStartMode();
                        gameView.updateStatus(msg);
                        gameController.setBlackjack(false);
                    } else {
                        gameView.switchSplitButton(gameController.isStateSplit());
                        gameView.switchDoubleButton(gameController.isStateDouble());
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
