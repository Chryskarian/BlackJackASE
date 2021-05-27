package model;

import java.awt.event.FocusListener;
import javax.swing.JPasswordField;

public final class PasswordFieldBuilder {

	private final JPasswordField field;

	private PasswordFieldBuilder() {
		field = new JPasswordField();
	}

	public static PasswordFieldBuilder builder() {
		return new PasswordFieldBuilder();
	}

	public PasswordFieldBuilder text(final String text) {
		field.setText(text);
		return this;
	}

	public PasswordFieldBuilder toolTipText(final String toolTipText) {
		field.setToolTipText(toolTipText);
		return this;
	}

	public PasswordFieldBuilder addFocusListener(final FocusListener focusListener) {
		field.addFocusListener(focusListener);
		return this;
	}

	public JPasswordField build() {
		return field;
	}

}
