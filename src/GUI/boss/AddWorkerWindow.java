package GUI.boss;

import GUI.BossGUI;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;
/**
 * @author Patryk Bocheński
 */
/**
 * Dialog window that allows the boss to add a new worker.
 * Provides input fields for worker details and sends the data to the server.
 */
public class AddWorkerWindow extends JDialog implements ActionListener {

    /** Button to trigger adding the worker */
    JButton addButton;

    /** Text field for worker's first name */
    JTextField nameField;

    /** Text field for worker's last name */
    JTextField surnameField;

    /** Text field for worker's age */
    JTextField ageField;

    /** Dropdown for selecting the worker's position */
    JComboBox<PositionEnum> positionCombo;

    /** Output stream to send objects to the server */
    private ObjectOutputStream objectOut;

    /** Reference to the parent BossGUI for styling */
    private BossGUI bossGUI;

    /**
     * Constructor that initializes the AddWorkerWindow.
     *
     * @param bossGUI   reference to the main BossGUI
     * @param parent    parent frame for modal dialog positioning
     * @param objectOut stream to send data to the server
     */
    public AddWorkerWindow(BossGUI bossGUI, JFrame parent, ObjectOutputStream objectOut) {
        super(parent, "Add Worker", true);
        this.bossGUI = bossGUI;
        this.objectOut = objectOut;
        init(parent);
    }

    /**
     * Initializes the window layout, components, styling, and event handling.
     * @param parent the parent JFrame used for positioning
     */
    public void init(JFrame parent) {
        setSize(800, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.DARK_GRAY);
        getContentPane().setForeground(Color.white);

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

        bossGUI.style(nameLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(surnameLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(ageLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(positionLabel, bossGUI.labelFont, bossGUI.labelColor, bossGUI.labelBg);
        bossGUI.style(nameField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(surnameField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(ageField, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.style(positionCombo, bossGUI.inputFont, bossGUI.labelColor, bossGUI.inputBg);
        bossGUI.styleButton(addButton);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "addWorker");
        getRootPane().getActionMap().put("addWorker", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton.doClick();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    objectOut.writeObject("CLOSE_ADD_WORKER");
                    objectOut.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(AddWorkerWindow.this, ex.getMessage());
                }
            }
        });
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            handleAddWorker();
        }
    }

    /**
     * Collects and validates user input, creates a Worker object,
     * and sends it to the server via objectOut.
     */
    private void handleAddWorker() {
        try {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String ageText = ageField.getText().trim();

            if (name.isEmpty() || surname.isEmpty() || ageText.isEmpty())  {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }
            if (name.matches(".*\\d+.*") || surname.matches(".*\\d+.*"))  {
                JOptionPane.showMessageDialog(this, "Name and Surname cannot contain numbers!");
                return;
            }
            int age = Integer.parseInt(ageText);
            if (age <= 17 || age >= 100) {
                JOptionPane.showMessageDialog(this, "Age must be between 18 and 99.");
                return;
            }
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to add worker: "+ name + " " + surname + " " + ageText + " ?",
                    "Confirm add",
                    JOptionPane.YES_NO_OPTION);

            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }

            PositionEnum position = (PositionEnum) positionCombo.getSelectedItem();
            int id = 0;
            Object worker = switch (Objects.requireNonNull(position)) {
                case Boss -> new Boss(name, surname, age, id, position);
                case Cook -> new Cook(name, surname, age, id, position);
                case Waiter -> new Waiter(name, surname, age, id, position);
            };

            objectOut.writeObject(SortEnum.ADD);
            objectOut.flush();
            objectOut.writeObject(worker);
            objectOut.flush();
            clear();

            JOptionPane.showMessageDialog(AddWorkerWindow.this, "Worker Added!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for age.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage());
        }
    }

    /**
     * Clears all input fields in the form.
     */
    public void clear() {
        nameField.setText("");
        surnameField.setText("");
        ageField.setText("");
    }
}
