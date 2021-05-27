package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;

import databasecommunication.Player;
import model.GUIFactory;
import model.JLabelFactory;
import model.PasswordFieldBuilder;

@SuppressWarnings("serial")
public class LogInGUI extends JFrame {

	private JPanel logInPanel;
	private JTextField usernameTextField;
	private JPasswordField passwordPWF;
	private JLabel status;
	private JButton logInButton;
	private JButton registerButton;
	private JButton passwordRecoveryButton;
	private transient Player player;

	public LogInGUI() {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocation(600, 400);
		this.setSize(400, 400);
		this.setTitle("CasinoBew - Login");

		status = new JLabel("Bitte Anmelden");

		final LogInFocusListener focusListener = new LogInFocusListener();

		usernameTextField = GUIFactory.newTextField("Benutzer", focusListener, "");
		passwordPWF = PasswordFieldBuilder.builder() //
				.text("Passwort") //
				.toolTipText("Passwort") //
				.addFocusListener(focusListener).build();
		logInButton = GUIFactory.newButton("Anmelden", true);
		registerButton = GUIFactory.newButton("Registrieren", true);
		passwordRecoveryButton = GUIFactory.newButton("Passwort vergessen", true);

		logInPanel = new JPanel(new GridLayout(0, 1));

		logInPanel.add(status);
		logInPanel.add(JLabelFactory.builder().text("Benutzer: ").build());
		logInPanel.add(usernameTextField);
		logInPanel.add(JLabelFactory.builder().text("Passwort: ").build());
		logInPanel.add(passwordPWF);
		logInPanel.add(logInButton);
		logInPanel.add(registerButton);
		logInPanel.add(passwordRecoveryButton);

		this.add(logInPanel);
	}

	public void setLogInActionListener(final ActionListener al) {
		usernameTextField.addActionListener(al);
		passwordPWF.addActionListener(al);
		logInButton.addActionListener(al);

	}

	public void setPasswordRecoveryActionListener(final ActionListener al) {
		passwordRecoveryButton.addActionListener(al);

	}

	public void setRegisterActionListener(final ActionListener al) {
		registerButton.addActionListener(al);
	}

	public void updateStatus(final String msg) {
		status.setText(msg);
	}

	public String getUsername() {
		return usernameTextField.getText();
	}

	public String getPassword() {
		return new String(passwordPWF.getPassword());
	}

	public Player getPlayer() {
		return player;
	}

	private class LogInFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {

			if (e.getSource() instanceof JTextField) {
				((JTextField) e.getSource()).setText("");
				((JTextField) e.getSource()).setForeground(Color.BLACK);
			}
		}

		@Override
		public void focusLost(FocusEvent e) {

			if (e.getSource() instanceof JTextField && ((JTextField) e.getSource()).getText().equals("")) {

				((JTextField) e.getSource()).setText(((JTextField) e.getSource()).getToolTipText());
				((JTextField) e.getSource()).setForeground(Color.GRAY);
			}
		}
	}
}