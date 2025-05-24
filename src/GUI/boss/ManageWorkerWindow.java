package GUI.boss;

import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ManageWorkerWindow extends JDialog implements ActionListener {
    private JButton editButton, removeButton;
    private JButton manageBossesButton, manageManagersButton, manageCooksButton, manageWaitersButton, backButton, nextButton, saveButton;
    private JTextField nameField, surnameField, ageField;
    JComboBox<PositionEnum> positionCombo;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;

    private ArrayList<Person> currentList;
    private int currentIndex = 0;
    private String name;
    private String surname;
    private String ageText;

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
        PositionEnum[] positions = PositionEnum.values();
        positionCombo = new JComboBox<>(positions);
        positionCombo.setEditable(false);
        positionCombo.setEnabled(false);
        positionCombo.setBounds(240, 230, 420, 30);
        add(positionLabel);
        add(positionCombo);

        editButton = new JButton("Edit");
        editButton.setBounds(240, 300, 100, 40);
        add(editButton);
        editButton.addActionListener(this);

        removeButton = new JButton("Remove");
        removeButton.setBounds(360, 300, 100, 40);
        add(removeButton);
        removeButton.addActionListener(this);

        backButton = new JButton("Back");
        backButton.setBounds(480, 300, 80, 40);
        backButton.addActionListener(this);
        add(backButton);
        nextButton = new JButton("Next");
        nextButton.setBounds(570, 300, 80, 40);
        add(nextButton);
        nextButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.setBounds(240, 350, 100, 40);
        add(saveButton);
        saveButton.addActionListener(this);
        saveButton.setVisible(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        SortEnum sort = null;
        if (source == manageBossesButton) {
            sort = SortEnum.BOSS;
            loadDate(sort);
        } else if (source == manageManagersButton) {
            sort = SortEnum.MANAGER;
            loadDate(sort);
        } else if (source == manageCooksButton) {
            sort = SortEnum.COOK;
            loadDate(sort);
        } else if (source == manageWaitersButton) {
            sort = SortEnum.WAITER;
            loadDate(sort);
        } else if (source == nextButton) {
            if (currentList != null) {
                currentIndex = (currentIndex + 1) % currentList.size();
                displayPersonData(currentList.get(currentIndex));
            } else {
                nextButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        } else if (source == backButton) {
            if(currentList != null) {
                if (currentIndex > 0) {
                    currentIndex--;
                }
            } else
            {
                nextButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
            displayPersonData(currentList.get(currentIndex));
        } else if (source == editButton) {
            handleEditWorker();
        } else if (source == saveButton) {
            handleSaveWorker();
        } else if (source == removeButton) {
            handleRemoveWorker();
        }
    }

    public void handleRemoveWorker() {
        Person temp = currentList.get(currentIndex);
        currentList.remove(currentIndex);

        if (currentList.isEmpty()) {
            nameField.setText("");
            surnameField.setText("");
            ageField.setText("");
            positionCombo.removeAllItems();
            removeButton.setEnabled(false);
            editButton.setEnabled(false);

        } else if (currentIndex >= currentList.size()) {
            currentIndex = currentList.size() - 1;
            displayPersonData(currentList.get(currentIndex));
        } else {
            displayPersonData(currentList.get(currentIndex));
        }
        try {
            objectOut.writeObject(SortEnum.REMOVE);
            objectOut.flush();
            objectOut.writeObject(temp);
            objectOut.flush();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }
    }

    public void handleSaveWorker() {
        backButton.setVisible(false);
        nextButton.setVisible(false);
        manageWaitersButton.setVisible(false);
        manageBossesButton.setVisible(false);
        manageManagersButton.setVisible(false);
        manageCooksButton.setVisible(false);
        name = nameField.getText().trim();
        surname = surnameField.getText().trim();
        ageText = ageField.getText().trim();

        if (name.isEmpty() || surname.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }

        int age = Integer.parseInt(ageText);
        if (age <= 0 || age >= 100) {
            JOptionPane.showMessageDialog(this, "Age must be between 1 and 99.");
            return;
        }
        PositionEnum position = (PositionEnum) positionCombo.getSelectedItem();
        Person original = currentList.get(currentIndex);
        int id = original.getId();
        Person updated = switch (position) {

            case Boss -> new Boss(name, surname, age, id, position);
            case Manager -> new Manager(name, surname, age, id, position);
            case Cook -> new Cook(name, surname, age, id, position);
            case Waiter -> new Waiter(name, surname, age, id, position);
        };
        currentList.set(currentIndex, updated);
        Person edited = currentList.get(currentIndex);
        if (!original.getPosition().equals(updated.getPosition())) {
            currentList.remove(currentIndex);
            try {
                objectOut.writeObject(SortEnum.SAVE);
                objectOut.flush();
                objectOut.writeObject(original);
                objectOut.flush();
                objectOut.writeObject(updated);
                objectOut.flush();
                objectOut.writeObject(currentList);
                objectOut.flush();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            if (currentList.isEmpty()) {
                clear();
            } else {
                if (currentIndex >= currentList.size()) {
                    currentIndex = currentList.size() - 1;
                }
                displayPersonData(currentList.get(currentIndex));
            }

            JOptionPane.showMessageDialog(this, "Worker has changed position. Refreshing list.");
            return;
        }

        nameField.setEditable(false);
        surnameField.setEditable(false);
        ageField.setEditable(false);
        saveButton.setVisible(false);
        backButton.setVisible(true);
        nextButton.setVisible(true);
        displayPersonData(currentList.get(currentIndex));
        manageWaitersButton.setVisible(true);
        manageBossesButton.setVisible(true);
        manageManagersButton.setVisible(true);
        manageCooksButton.setVisible(true);
    }

    public void handleEditWorker() {
        saveButton.setVisible(true);
        nameField.setEditable(true);
        surnameField.setEditable(true);
        ageField.setEditable(true);
        positionCombo.setEnabled(true);

    }

    public void clear() {
        nameField.setText("");
        surnameField.setText("");
        ageField.setText("");
        positionCombo.removeAllItems();
        editButton.setEnabled(false);
        removeButton.setEnabled(false);
    }

    private void loadDate(SortEnum sort) {
        try {
            currentIndex = 0;
            loadList(sort);

            if (!currentList.isEmpty()) {
                displayPersonData(currentList.get(currentIndex));
                editButton.setEnabled(true);
                removeButton.setEnabled(true);
            } else {
                editButton.setEnabled(false);
                removeButton.setEnabled(false);
                clear();
                JOptionPane.showMessageDialog(this, "No " + sort.toString().toLowerCase() + "s found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ManageWorkerWindow.this, "Error loading data: " + ex.getMessage(), "[LOAD DATA] Error", JOptionPane.ERROR_MESSAGE);
            clear();
        }
    }

    private void loadList(SortEnum sort) throws Exception {
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

    private void displayPersonData(Person person) {
        if (person != null) {
            nameField.setText(person.getName());
            surnameField.setText(person.getSurname());
            ageField.setText(String.valueOf(person.getAge()));
            positionCombo.setSelectedItem(person.getPosition());
        }
    }
}