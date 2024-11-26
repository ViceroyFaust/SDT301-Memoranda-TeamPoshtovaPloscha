package memoranda.ui;

import memoranda.util.Configuration;
import memoranda.util.CurrentStorage;
import memoranda.util.Local;
import memoranda.util.MimeTypesList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/*$Id: PreferencesDialog.java,v 1.16 2006/06/28 22:58:31 alexeya Exp $*/
public class DriverCreateDialog extends JDialog {

	// UI components
	private JTextField textField1;
	private JTextField textField2;
	private JButton closeButton;
	private JButton actionButton;

	public DriverCreateDialog(Frame frame) {
		super(frame, "Add new driver", true);
		initUI();
	}

	public DriverCreateDialog() {
		this(null);
	}

	private void initUI() {
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// First Text Field
		textField1 = new JTextField(20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(new JLabel("Name"), gbc);
		gbc.gridx = 1;
		this.add(textField1, gbc);

		// Second Text Field
		textField2 = new JTextField(20);
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(new JLabel("Phone number"), gbc);
		gbc.gridx = 1;
		this.add(textField2, gbc);

		// Close Button
		closeButton = new JButton("Close");
		closeButton.addActionListener(e -> this.dispose());
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(closeButton, gbc);

		// Action Button
		actionButton = new JButton("Add to catalog");
		actionButton.addActionListener(e -> {
			// Empty action for now
		});
		gbc.gridx = 1;
		this.add(actionButton, gbc);

		this.pack();
		this.setLocationRelativeTo(null); // Center on screen
	}
}
