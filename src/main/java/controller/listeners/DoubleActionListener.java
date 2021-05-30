package controller.listeners;

import blackjackobjects.Money;
import blackjackobjects.Person;
import controller.GameController;
import databasecommunication.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import model.handlers.CardHandler;
import model.enums.WinSituation;
import view.GameGUI;

public class DoubleActionListener implements ActionListener {

    private final GameController gameController;

    public DoubleActionListener(final GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player player = gameController.getPlayer();
        Person croupier = gameController.getCroupier();
        CardHandler cardHandler = gameController.getCardHandler();
        GameGUI gameView = gameController.getGameView();

        player.removeFromBalance(player.getBetAmount());
        player.setBetAmount(Money.multiply(player.getBetAmount(), 2));
        cardHandler.givePlayingCard(player);
        cardHandler.croupierTurn(croupier);

        WinSituation winSituation = gameController.getEvaluationHandler().evaluateStopNormal(player, croupier);

        SwingUtilities.invokeLater(() -> {
            gameView.setPlayerBetAmount(player.getBetAmount());
            gameView.setPlayerBalance(player.getBalance());
            gameView.addPlayerCard(player.getLastCardOnHand().getCardImage());
            gameView.setPlayerPoints(player.getPointsOnHand());
            gameController.drawCroupierCards(winSituation.getMessage());
        });
    }
}
