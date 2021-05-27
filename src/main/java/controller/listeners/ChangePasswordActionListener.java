package controller.listeners;

import controller.GameController;
import databasecommunication.Password;
import view.GameGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangePasswordActionListener implements ActionListener {
    private final Logger logger = Logger.getLogger(ChangePasswordActionListener.class.getName());

    private final GameController gameController;

    public ChangePasswordActionListener(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        GameGUI gameView = gameController.getGameView();

        if (changePassword(gameView.getPassword())) {
            SwingUtilities.invokeLater(() -> {
                gameView.updateProfileStatus("Ihr Passwort wurde erfolgreich geändert", true);
                gameView.setProfileChangePasswordTF("");
            });
        } else {
            SwingUtilities.invokeLater(() -> gameView.updateProfileStatus("Fehler beim ändern des Passwortes", false));
        }
    }

    private boolean changePassword(String password) {
        String[] newSaltItarationsPassword = null;
        String newHashedPassword;

        try {
            newHashedPassword = Password.generateStrongPasswordHash(password);
            newSaltItarationsPassword = newHashedPassword.split(":");

            logger.log(Level.INFO, "Passwort wurde geändert");
            gameController.getPlayer().setIterations(Integer.parseInt(newSaltItarationsPassword[0]));
            gameController.getPlayer().setSalt(newSaltItarationsPassword[1]);
            gameController.getPlayer().setPassword(newSaltItarationsPassword[2]);
            return true;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.log(Level.WARNING, "Konnte kein neues Passwort erstellen", e);
            return false;
        }
    }
}
