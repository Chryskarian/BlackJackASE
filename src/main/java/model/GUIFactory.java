package model;

import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

public final class GUIFactory {

	private GUIFactory() {
		// Kann leer sein
	}

	/**
	 * Methode zum erstellen von Buttons in der GUI
	 * 
	 * @param title  der Text der auf dem Knopfes stehen soll
	 * @param active setzt den status den Knopfes, ob der gedrückt werden kann oder
	 *               nicht
	 * @return btn (Jbutton)
	 */

	public static JButton newButton(final String title, boolean active) {
		final JButton btn = new JButton(title);
		btn.setEnabled(active);
		return btn;
	}

	/**
	 * Methode zum erstellen von Textfeldern in der GUI
	 * 
	 * @param toolTip  Setzt den Text der angezeigt wird, wenn der Mauszeiger über
	 *                 dem Feld ist
	 * @param listener Setzt den Focuslistener
	 * @param text     Setzt den Text der im TextField stehen soll
	 * @return textField (JtextField)
	 */

	public static JTextField newTextField(final String toolTip, FocusListener listener, String text) {
		final JTextField textField = new JTextField();
		textField.setToolTipText(toolTip);
		textField.setText(text);
		textField.addFocusListener(listener);
		return textField;
	}

	/**
	 * Methode zum erstellen eines MenuItems, das einer JMenuBar hinzugefügt werden
	 * kann
	 * 
	 * @param title Setzt den Text des MenuItems
	 * @return MenuItem (JMenuItem)
	 */

	public static JMenuItem newMenuItem(final String title) {
		return new JMenuItem(title);
	}

}
