package controller.listeners;

import blackjackobjects.Money;
import controller.GameController;
import databasecommunication.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import view.GameGUI;

public class SplitActionListener implements ActionListener {

    private final GameController gameController;

    public SplitActionListener(final GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Player player = gameController.getPlayer();
        GameGUI gameView = gameController.getGameView();

        // Spieler Einstellungen für Split werden getroffen
        prepareSplitRound(player);
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

    private void prepareSplitRound(final Player player) {
        player.setSplitPlayerPoints(player.getLastCardOnHand().getCardValue());
        player.setPointsOnHand(player.getPointsOnHand() - player.getLastCardOnHand().getCardValue());
        player.addToSplitPlayerHand(player.getLastCardOnHand());
        player.getHand().remove(player.getLastCardOnHand());
        player.removeFromBalance(player.getBetAmount());
        player.setBetAmount(Money.multiply(player.getBetAmount(), 2));
        player.setSplit(true);
    }
}
