package GUI.boss;

import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Objects;

public class AddWorkerWindow extends JDialog implements ActionListener {
    JButton addButton;
    JTextField nameField, surnameField, ageField;
    JComboBox<PositionEnum> positionCombo;

    private ObjectOutputStream objectOut;

    public AddWorkerWindow(JFrame parent,ObjectOutputStream objectOut) {
        super(parent, "Add Worker", true);
        this.objectOut = objectOut;
        init(parent);
    }

    public void init(JFrame parent) {
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

        nameField = new JTextField();
        nameField.setBounds(240, 50, 420, 30);
        add(nameField);

        surnameField = new JTextField();
        surnameField.setBounds(240, 100, 420, 30);
        add(surnameField);

        ageField = new JTextField();
        ageField.setBounds(240, 150, 420, 30);
        add(ageField);

        addButton = new JButton("Add");
        addButton.setBounds(240, 240, 100, 40);
        add(addButton);
        addButton.addActionListener(this);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(100, 200, 120, 30);
        PositionEnum[] positions = PositionEnum.values();
        positionCombo = new JComboBox<>(positions);
        positionCombo.setBounds(240, 200, 420, 30);
        add(positionLabel);
        add(positionCombo);

        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            try {
                String name = nameField.getText();
                String surname = surnameField.getText();
                int age = Integer.parseInt(ageField.getText());
                PositionEnum position = (PositionEnum) positionCombo.getSelectedItem();
                int id = 0;
               Object worker = switch (Objects.requireNonNull(position)) {
                    case Boss -> new Boss(name, surname, age, id, PositionEnum.Boss);
                    case Manager -> new Manager(name, surname, age, id, PositionEnum.Manager);
                    case Cook -> new Cook(name, surname, age, id, PositionEnum.Cook);
                    case Waiter -> new Waiter(name, surname, age, id, PositionEnum.Waiter);
                };
               objectOut.writeObject("ADD");
               objectOut.flush();
               objectOut.writeObject(worker);
               objectOut.flush();
               dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for age.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage());
            }
        }
    }
}

