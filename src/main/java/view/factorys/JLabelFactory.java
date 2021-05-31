package view.factorys;

import javax.swing.JLabel;

public final class JLabelFactory {

	private final JLabel label;

	private JLabelFactory() {
		label = new JLabel();
	}

	public static JLabelFactory builder() {
		return new JLabelFactory();
	}

	public JLabelFactory text(final String text) {
		label.setText(text);
		return this;
	}

	public JLabelFactory setVisible(final boolean visible) {
		label.setVisible(visible);
		return this;
	}

	public JLabelFactory setEnabled(final Boolean enabled) {
		label.setEnabled(enabled);
		return this;
	}

	public JLabel build() {
		return label;
	}

}
