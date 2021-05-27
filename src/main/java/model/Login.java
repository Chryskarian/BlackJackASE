package model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import blackjackobjects.Money;
import controller.GameController;
import databasecommunication.HibernateDatabaseActions;
import databasecommunication.Password;
import databasecommunication.Player;

public class Login {

	private String error = "Error";
	private final Logger logger = Logger.getLogger(Login.class.getName());

	public boolean login(String username, String password) {
		Player player = HibernateDatabaseActions.anmelden(username);
		try {
			// Sollte ein null-Objekt kommen ist der benutzer falsch
			if (player == null) {
				return false;
				// Sollte false kommen, wurde falsches Passwort eingegeben
			} else if (!Password.validatePassword(password, player.getPassword(), player.getSalt(), player.getIterations())) {
				return false;
				// richtiger Benutzer und Passwort
			} else {
				GameController controllerG = new GameController(player);
				controllerG.showView();
				return true;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException validatePasswortevt) {
			logger.log(Level.WARNING, "Passwort Validierungs Fehler", validatePasswortevt);
			return false;
		}
	}

	public String recoverPassword(String username) {
		String[] newSaltIterationPassword = null;
		String newPassword = null;

		try {
			newPassword = Password.randomPassword();
			String newHashedPassword = Password.generateStrongPasswordHash(newPassword);
			newSaltIterationPassword = newHashedPassword.split(":");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException validatePasswortevt) {
			logger.log(Level.WARNING, "Passwort Erstellungs Fehler", validatePasswortevt);
			return error;
		}
		if (newSaltIterationPassword != null) {
			HibernateDatabaseActions.passwortVergessen(username, newSaltIterationPassword[2], newSaltIterationPassword[1], Integer.parseInt(newSaltIterationPassword[0]));
			return "Ihr neues Passord lautet: " + newPassword;
		}
		return error;
	}

	public String registerUser(String username, String password) {

		if (HibernateDatabaseActions.anmelden(username) != null) {
			return "Benutzer existiert bereits";
		} else {
			String[] hashAndSalt = null;
			try {
				String hashedPassword = Password.generateStrongPasswordHash(password);
				hashAndSalt = hashedPassword.split(":");
			} catch (NoSuchAlgorithmException | InvalidKeySpecException generatePasswortevt) {
				logger.log(Level.WARNING, "Passwort generieren Fehler", generatePasswortevt);
				return error;
			}
			// Sollte dies Funktionieren wird der neue Benutzer mit informationen gefüllt
			// und per Hibernate der datenbank übergeben
			if (hashAndSalt != null) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				Player newUser = new Player();
				newUser.setUsername(username);
				newUser.setPassword(hashAndSalt[2]);
				newUser.setSalt(hashAndSalt[1]);
				newUser.setIterations(Integer.parseInt(hashAndSalt[0]));
				newUser.setPlayerBank(Money.toMoney(1000));
				newUser.setTimeUntilMoneyIsAvailable(timestamp);
				HibernateDatabaseActions.registerUser(newUser);
				return "Benutzer wurde angelegt";
			}
			return error;
		}
	}

}
