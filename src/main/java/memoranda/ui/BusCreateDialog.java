package memoranda.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BusCreateDialog extends JDialog {

    private JTextField seatsField;
    private JComboBox<String> shrinkBar;
    private JButton closeButton;
    private JButton saveButton;

    public BusCreateDialog(Frame frame) {
        super(frame, "Create Bus", true);
        initUI();
    }

    public BusCreateDialog() {
        this(null);
    }

    private void initUI() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for Seats Input
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Seats:"), gbc);

        // Text Field for Seats
        seatsField = new JTextField(20);
        gbc.gridx = 1;
        this.add(seatsField, gbc);

        // Label for Shrink Bar
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Driver:"), gbc);

        // Shrink Bar (JComboBox)
        String[] shrinkOptions = {"Vasyl Kropovskyi", "That guy from Dessert Bus", "Rocky Robinson"};
        shrinkBar = new JComboBox<>(shrinkOptions);
        gbc.gridx = 1;
        this.add(shrinkBar, gbc);

        // Close Button
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(closeButton, gbc);

        // Save Button
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save action logic (currently empty)
            }
        });
        gbc.gridx = 1;
        this.add(saveButton, gbc);

        this.pack();
        this.setLocationRelativeTo(null); // Center on screen
    }

    // Getter for retrieving user input (Optional if needed)
    public String getSeatsInput() {
        return seatsField.getText();
    }

    public String getSelectedOption() {
        return (String) shrinkBar.getSelectedItem();
    }
}
