package GUI.boss;

import model.PositionEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

public class ManageWorkerWindow extends JDialog implements ActionListener {
    private JButton editButton,removeButton;
    private JTextField nameField,surnameField,ageField;
    JComboBox<PositionEnum> positionCombo;
    public ManageWorkerWindow(JFrame parent) {
        super(parent, "Add Worker", true);
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
        nameField.setEditable(false);
        nameField.setBounds(240, 50, 420, 30);
        add(nameField);

        surnameField = new JTextField();
        surnameField.setEditable(false);
        surnameField.setBounds(240, 100, 420, 30);
        add(surnameField);

        ageField = new JTextField();
        ageField.setEditable(false);
        ageField.setBounds(240, 150, 420, 30);
        add(ageField);

        editButton = new JButton("Edit");
        editButton.setBounds(240, 240, 100, 40);
        add(editButton);

        removeButton = new JButton("Remove");
        removeButton.setBounds(380, 240, 100, 40);
        add(removeButton);

        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(100, 200, 120, 30);
        positionCombo = new JComboBox<>();
        positionCombo.setEditable(false);
        positionCombo.setBounds(240, 200, 420, 30);
        add(positionLabel);
        add(positionCombo);
        setVisible(true);



    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == editButton) {

        }

    }
}
