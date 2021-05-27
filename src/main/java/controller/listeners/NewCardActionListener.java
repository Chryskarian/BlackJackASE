package controller.listeners;

import blackjackobjects.Person;
import controller.GameController;
import databasecommunication.Player;
import model.enums.WinSituation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewCardActionListener implements ActionListener {

    private final GameController gameController;

    public NewCardActionListener(final GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameController.getCardHandler().givePlayingCard(gameController.getPlayer());
        switch (gameController.getCardHandler().checkIfBust(gameController.getPlayer())) {
            case NEIN:
                notOverdrawn();
                break;
            case JA:
                overdrawn();
                break;
            case HAND2JA:
                hand2Overdrawn();
                break;
            default:
                break;
        }
    }

    private void notOverdrawn() {
        SwingUtilities.invokeLater(() -> {
            gameController.getGameView().switchDoubleButton(false);
            gameController.getGameView().switchSplitButton(false);
            if (gameController.getPlayer().isHand2()) {
                addSplitPlayerCard();
            } else {
                addNormalPlayerCard();
            }
        });
    }

    private void overdrawn() {
        SwingUtilities.invokeLater(() -> {
            addNormalPlayerCard();
            String msg;
            if (!gameController.getPlayer().isSplit()) {
                gameController.getGameView().setPlayerBalance(gameController.getPlayer().getBalance());
                gameController.switchButtonsToStartMode();
                msg = "Sie haben überzogen!";
            } else {
                gameController.getPlayer().setHand2(true);
                msg = "Hand 2 wird gespielt";
            }
            gameController.getGameView().updateStatus(msg);
        });
    }

    private void hand2Overdrawn() {
        Person croupier = gameController.getCroupier();

        gameController.getCardHandler().croupierTurn(croupier);
        Player player = gameController.getPlayer();

        WinSituation winSituation = gameController.getEvaluationHandler().evaluateStopSplit(player, croupier);
        gameController.getMoneyHandler().moneyBasedOnWinSituation(winSituation, player);
        SwingUtilities.invokeLater(() -> {
            addSplitPlayerCard();
            gameController.drawCroupierCards(winSituation.getMessage());
        });
    }

    private void addNormalPlayerCard() {
        gameController.getGameView().addPlayerCard(gameController.getPlayer().getLastCardOnHand().getCardImage());
        gameController.getGameView().setPlayerPoints(gameController.getPlayer().getPointsOnHand());
    }

    private void addSplitPlayerCard() {
        gameController.getGameView().addSplitCard(gameController.getPlayer().getLastSplitPlayerCard().getCardImage());
        gameController.getGameView().setSplitPlayerPoints(gameController.getPlayer().getSplitPlayerPoints());
    }
}
