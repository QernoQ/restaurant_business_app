package GUI.boss;

import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ManageWorkerWindow extends JDialog implements ActionListener {
    private JButton editButton, removeButton;
    private JButton manageBossesButton, manageManagersButton, manageCooksButton, manageWaitersButton;
    private JTextField nameField, surnameField, ageField;
    JComboBox<PositionEnum> positionCombo;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;

    private ArrayList<Person> currentList;
    private int currentIndex = 0;

    public ManageWorkerWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Add Worker", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.currentList = new ArrayList<>();
        init(parent);
    }

    public void init(JFrame parent) {
        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);


        manageBossesButton = new JButton("Manage Bosses");
        manageBossesButton.setBounds(50, 20, 150, 35);
        add(manageBossesButton);

        manageManagersButton = new JButton("Manage Managers");
        manageManagersButton.setBounds(220, 20, 150, 35);
        add(manageManagersButton);

        manageCooksButton = new JButton("Manage Cooks");
        manageCooksButton.setBounds(390, 20, 150, 35);
        add(manageCooksButton);

        manageWaitersButton = new JButton("Manage Waiters");
        manageWaitersButton.setBounds(560, 20, 150, 35);
        add(manageWaitersButton);

        manageBossesButton.addActionListener(this);
        manageManagersButton.addActionListener(this);
        manageCooksButton.addActionListener(this);
        manageWaitersButton.addActionListener(this);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(100, 80, 120, 30);
        add(nameLabel);

        JLabel surnameLabel = new JLabel("Surname:");
        surnameLabel.setBounds(100, 130, 120, 30);
        add(surnameLabel);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(100, 180, 120, 30);
        add(ageLabel);

        nameField = new JTextField();
        nameField.setEditable(false);
        nameField.setBounds(240, 80, 420, 30);
        add(nameField);

        surnameField = new JTextField();
        surnameField.setEditable(false);
        surnameField.setBounds(240, 130, 420, 30);
        add(surnameField);

        ageField = new JTextField();
        ageField.setEditable(false);
        ageField.setBounds(240, 180, 420, 30);
        add(ageField);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(100, 230, 120, 30);
        positionCombo = new JComboBox<>();
        positionCombo.setEditable(false);
        positionCombo.setBounds(240, 230, 420, 30);
        add(positionLabel);
        add(positionCombo);

        editButton = new JButton("Edit");
        editButton.setBounds(240, 280, 100, 40);
        add(editButton);

        removeButton = new JButton("Remove");
        removeButton.setBounds(380, 280, 100, 40);
        add(removeButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        String sort = null;
        int page = 0;
        if (source == manageBossesButton) sort = "BOSS";
        else if (source == manageManagersButton) sort = "MANAGER";
        else if (source == manageCooksButton) sort = "COOK";
        else if (source == manageWaitersButton) sort = "WAITER";

    if (sort != null) {
        try {
            loadWorkerList(sort);
            if (!currentList.isEmpty()) {
                currentIndex = 0;
                displayPersonData(currentList.get(currentIndex));
            } else {
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }
}

    private void loadWorkerList(String sort) throws Exception {
        objectOut.writeObject(sort);
        objectOut.flush();

        Object response = objectIn.readObject();

        if (response instanceof ArrayList<?> list) {
            currentList.clear();
            for (Object obj : list) {
                if (obj instanceof Person person) {
                    currentList.add(person);
                }
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        surnameField.setText("");
        ageField.setText("");
        if (positionCombo.getItemCount() > 0) {
            positionCombo.setSelectedIndex(0);
        }
    }

    private void displayPersonData(Person person) {
        nameField.setText(person.getName());
        surnameField.setText(person.getSurname());
        ageField.setText(String.valueOf(person.getAge()));
        positionCombo.setSelectedItem(person.getPosition());
    }
}