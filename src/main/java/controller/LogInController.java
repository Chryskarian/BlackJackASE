package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import model.Login;

import view.LogInGUI;

class LogInController {

	private LogInGUI logInView;
	private Login login;

	public LogInController() {
		this.login = new Login();
		this.logInView = new LogInGUI();
		addListener();
	}

	void showView() {
		// macht die AnmeldeGUI sichtbar
		this.logInView.setVisible(true);
	}

	private void addListener() {
		// Fügt den Buttons ihre Listeners hinzu
		this.logInView.setLogInActionListener(new LogInActionListener());
		this.logInView.setPasswordRecoveryActionListener(new PasswordRecoveryActionListener());
		this.logInView.setRegisterActionListener(new RegisterActionListener());
	}

	private class LogInActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (login.login(logInView.getUsername(), logInView.getPassword())) {
				logInView.dispose();
			} else {
				SwingUtilities.invokeLater(() -> logInView.updateStatus("Falscher Benutzer oder Passwort"));
			}
		}
	}

	private class PasswordRecoveryActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = login.recoverPassword(logInView.getUsername());
			SwingUtilities.invokeLater(() -> logInView.updateStatus(msg));
		}
	}

	private class RegisterActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = login.registerUser(logInView.getUsername(), logInView.getPassword());
			SwingUtilities.invokeLater(() -> logInView.updateStatus(msg));
		}
	}
}