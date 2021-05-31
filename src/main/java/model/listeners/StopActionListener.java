package model.listeners;

import blackjackobjects.Person;
import controller.GameController;
import databasecommunication.Player;
import model.handlers.EvaluationHandler;
import model.enums.WinSituation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopActionListener implements ActionListener {

    private final GameController gameController;

    public StopActionListener(final GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player player = gameController.getPlayer();
        Person croupier = gameController.getCroupier();
        EvaluationHandler evaluationHandler = gameController.getEvaluationHandler();

        if (player.isSplit()) {
            player.setHand2(true);
            SwingUtilities.invokeLater(() -> gameController.getGameView().updateStatus("Hand 2 wird gespielt"));
        } else {
            gameController.getCardHandler().croupierTurn(croupier);
            WinSituation winSituation;
            if (player.isSplit()) {
                winSituation = evaluationHandler.evaluateStopSplit(player, croupier);
            } else {
                winSituation = evaluationHandler.evaluateStopNormal(player, croupier);
            }
            gameController.getMoneyHandler().addMoneyBasedOnWinSituation(winSituation, player);
            SwingUtilities.invokeLater(() -> {
                gameController.drawCroupierCards(winSituation.getMessage());
            });
        }
    }
}
