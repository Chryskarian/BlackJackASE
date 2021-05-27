package controller.listeners;

import blackjackobjects.Money;
import controller.GameController;
import databasecommunication.Player;
import view.GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfileGameStartActionListener implements ActionListener {

    private final GameController gameController;

    public ProfileGameStartActionListener(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GameGUI gameView = gameController.getGameView();
        try {
            startGame(gameView.getProfileStartGameTF());

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

    private void startGame(double startMoney) {
        Player player = gameController.getPlayer();

        if (startMoney <= 0 || Money.compare(player.getPlayerBank(), startMoney) > 0) {
            throw new IllegalArgumentException("<=0 oder >playerBank");
        }
        player.setBalance(Money.toMoney(startMoney));
        player.setPlayerBank(Money.subtract(player.getPlayerBank(), startMoney));
        player.setLoggedIn(true);
    }

}
