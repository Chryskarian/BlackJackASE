package controller.listeners;

import controller.GameController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMoneyActionListener implements ActionListener {

    private final GameController gameController;

    public AddMoneyActionListener(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameController.getMoneyHandler().giftMoney(gameController.getPlayer());
        SwingUtilities.invokeLater(() -> {
            gameController.getGameView().setProfileBalance(gameController.getPlayer().getPlayerBank().toString());
            gameController.getGameView().updateProfileStatus("Wir schenken Ihnen 1000€.", true);
        });
    }
}
