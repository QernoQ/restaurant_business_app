package GUI.boss;

import javax.swing.*;
import java.awt.*;

public class AddWorkerWindow extends JDialog {

    public AddWorkerWindow(JFrame parent) {
        super(parent, "Add Worker", true);
        setSize(800, 400);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 50, 120, 30);
        add(nameLabel);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(100, 100, 120, 30);
        add(surnameLabel);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(100, 150, 120, 30);
        add(ageLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(240, 50, 420, 30);
        add(nameField);

        JTextField surnameField = new JTextField();
        surnameField.setBounds(240, 100, 420, 30);
        add(surnameField);

        JTextField ageField = new JTextField();
        ageField.setBounds(240, 150, 420, 30);
        add(ageField);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(280, 220, 100, 40);
        add(editButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(420, 220, 100, 40);
        add(removeButton);

        setVisible(true);
    }
}
