package controller.listeners;

import controller.GameController;
import view.GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TakeMoneyFromBankActionListener implements ActionListener {

    private final GameController gameController;

    public TakeMoneyFromBankActionListener(GameController gameController) {
        this.gameController = gameController;
    }

    public void actionPerformed(ActionEvent e) {
        GameGUI gameView = gameController.getGameView();

        try {
            gameController.getMoneyHandler().takeMoneyFromBank(gameController.getPlayer(), gameView.getProfileAddBalanceTF());
            SwingUtilities.invokeLater(() -> {
                gameView.setProfileBalance(gameController.getPlayer().getPlayerBank().toString());
                gameView.setPlayerBalance(gameView.getPlayer().getBalance());
                gameView.updateProfileStatus("Guthaben aufgeladen", true);
            });
        } catch (IllegalArgumentException e2) {
            SwingUtilities.invokeLater(() -> gameView.updateProfileStatus("Bitte richtigen Betrag eingeben", false));
        }
    }

}
