package GUI.boss;

import GUI.BossGUI;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ManageWorkerWindow extends JDialog implements ActionListener {
    private JButton editButton, removeButton;
    private JButton manageBossesButton, manageCooksButton, manageWaitersButton, backButton, nextButton, saveButton;
    private JTextField nameField, surnameField, ageField;
    JComboBox<PositionEnum> positionCombo;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;

    private ArrayList<Person> currentList;
    private int currentIndex = 0;
    private String name;
    private String surname;
    private String ageText;

    private BossGUI bossGUI;

    public ManageWorkerWindow(BossGUI bossGUI,JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Add Worker", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.bossGUI = bossGUI;
        this.currentList = new ArrayList<>();
        init(parent);
    }

    public void init(JFrame parent) {
        setSize(800, 450);
        setLocationRelativeTo(parent);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.DARK_GRAY);
        getContentPane().setForeground(Color.white);

        manageBossesButton = new JButton("Manage Bosses");
        manageBossesButton.setBounds(155, 20, 150, 35);
        add(manageBossesButton);

        manageCooksButton = new JButton("Manage Cooks");
        manageCooksButton.setBounds(325, 20, 150, 35);
        add(manageCooksButton);

        manageWaitersButton = new JButton("Manage Waiters");
        manageWaitersButton.setBounds(495, 20, 150, 35);
        add(manageWaitersButton);


        manageBossesButton.addActionListener(this);
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
        Font labelFont = new Font("Serif", Font.BOLD, 14);
        Font inputFont = new Font("Serif", Font.PLAIN, 14);
        Color labelColor = Color.WHITE;
        Color inputBg = new Color(60, 60, 60);
        Color labelBg = new Color(100, 100, 100);
        bossGUI.style(nameLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(surnameLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(ageLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(positionLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(nameField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(surnameField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(ageField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(positionCombo, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.styleButton(editButton);
        bossGUI.styleButton(saveButton);
        bossGUI.styleButton(removeButton);
        bossGUI.styleButton(nextButton);
        bossGUI.styleButton(backButton);
        bossGUI.styleButton(manageBossesButton);
        bossGUI.styleButton(manageCooksButton);
        bossGUI.styleButton(manageWaitersButton);


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    objectOut.writeObject("CLOSE_MANAGE_WORKER");
                    objectOut.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(ManageWorkerWindow.this, ex.getMessage());
                }
            }
        });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        SortEnum sort = null;
        if (source == manageBossesButton) {
            sort = SortEnum.BOSS;
            loadDate(sort);
        } else if (source == manageCooksButton) {
            sort = SortEnum.COOK;
            loadDate(sort);
        } else if (source == manageWaitersButton) {
            sort = SortEnum.WAITER;
            loadDate(sort);
        } else if (source == nextButton) {
            if (!currentList.isEmpty()) {
                currentIndex = (currentIndex + 1) % currentList.size();
                displayPersonData(currentList.get(currentIndex));
            }
        } else if (source == backButton) {
            if (!currentList.isEmpty()) {
                if (currentIndex > 0) {
                    currentIndex--;
                }
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
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this worker?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }
        Person temp = currentList.get(currentIndex);
        currentList.remove(temp);
        try {
            objectOut.writeObject(SortEnum.REMOVE);
            objectOut.flush();
            objectOut.writeObject(temp);
            objectOut.flush();

        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage());
            currentList.add(currentIndex,temp);
            return;
        }
        if (currentList.isEmpty()) {
            clear();
            enabled(false);
            JOptionPane.showMessageDialog(ManageWorkerWindow.this, "All workers have been removed!");
        } else if (currentIndex >= currentList.size()) {
            currentIndex = currentList.size() - 1;
            displayPersonData(currentList.get(currentIndex));
        } else {
            displayPersonData(currentList.get(currentIndex));
        }
    }

    public Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void handleSaveWorker() {
        name = nameField.getText().trim();
        surname = surnameField.getText().trim();
        ageText = ageField.getText().trim();

        if (name.isEmpty() || surname.isEmpty() || ageText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.");
            return;
        }
        Integer age = tryParseInt(ageText);
        if (age == null)
        {
            JOptionPane.showMessageDialog(ManageWorkerWindow.this, "Age must be a number!");
            return;
        }
        if (age <= 0 || age >= 100) {
            JOptionPane.showMessageDialog(this, "Age must be between 1 and 99.");
            return;
        }

        PositionEnum position = (PositionEnum) positionCombo.getSelectedItem();
        Person original = currentList.get(currentIndex);
        int id = original.getId();
        Person updated = switch (position) {

            case Boss -> new Boss(name, surname, age, id, position);
            case Cook -> new Cook(name, surname, age, id, position);
            case Waiter -> new Waiter(name, surname, age, id, position);
        };
        currentList.set(currentIndex, updated);
        Person edited = currentList.get(currentIndex);
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

        if (!original.getPosition().equals(updated.getPosition())) {
            currentList.remove(currentIndex);
            if (currentList.isEmpty()) {
                clear();
            } else {
                if (currentIndex >= currentList.size()) {
                    currentIndex = currentList.size() - 1;
                    saveButton.setVisible(false);
                }
                displayPersonData(currentList.get(currentIndex));
            }
            editable(false);
            positionCombo.setEnabled(false);
            visible(true);
            saveButton.setVisible(false);
            removeButton.setVisible(true);
            JOptionPane.showMessageDialog(this, "Worker has changed position. Refreshing list.");
            return;
        }
        saveButton.setVisible(false);
        editable(false);
        positionCombo.setEnabled(false);
        removeButton.setVisible(true);
        visible(true);
        JOptionPane.showMessageDialog(this, "Worker saved!");
        displayPersonData(currentList.get(currentIndex));
    }



    public void handleEditWorker() {
        if (currentList.isEmpty()) {
            saveButton.setVisible(false);
            enabled(false);
            editable(false);
        } else {
            saveButton.setVisible(true);
            enabled(true);
            visible(false);
            removeButton.setVisible(false);
            editable(true);
            positionCombo.setEnabled(true);
        }

    }

    public void clear() {
        nameField.setText("");
        surnameField.setText("");
        ageField.setText("");
        positionCombo.setSelectedIndex(-1);
    }

    private void loadDate(SortEnum sort) {
        try {
            currentIndex = 0;
            loadList(sort);

            if (!currentList.isEmpty()) {
                displayPersonData(currentList.get(currentIndex));
                enabled(true);
            } else {
                clear();
                enabled(false);
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
    public void visible(boolean visible) {
        backButton.setVisible(visible);
        nextButton.setVisible(visible);
        manageWaitersButton.setVisible(visible);
        manageBossesButton.setVisible(visible);
        manageCooksButton.setVisible(visible);
    }

    public void editable(boolean editable) {
        ageField.setEditable(editable);
        nameField.setEditable(editable);
        surnameField.setEditable(editable);
    }

    public void enabled(boolean enabled) {
        backButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
        editButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
    }
}