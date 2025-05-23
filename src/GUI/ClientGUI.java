package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientGUI extends BaseGUI implements ActionListener {
    private JButton loginButton;
    private JTextField loginTextField;

    public ClientGUI(String title, Socket socket) {
        super(title, socket);
        init();
    }

    public void init() {
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Container loginContainer = getContentPane();
        loginContainer.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.DARK_GRAY);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        Font labelFont = new Font("FlatLaf", Font.BOLD, 16);
        Font smallFont = new Font("FlatLaf", Font.PLAIN, 14);

        JLabel titleLabel = new JLabel("Login to Access");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(smallFont);
        loginLabel.setForeground(Color.WHITE);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginTextField = new JTextField();
        loginTextField.setMaximumSize(new Dimension(150, 25));
        loginTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginTextField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        loginButton = new JButton("Enter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(100, 30));

        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(loginLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginTextField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginButton);
        loginButton.addActionListener(this);
        loginPanel.add(Box.createVerticalGlue());
        loginContainer.add(loginPanel, BorderLayout.CENTER);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        String chooseMenu = loginTextField.getText().toLowerCase();
        if (source == loginButton) {
            out.println(chooseMenu);
            try {
                String temp = in.readLine();
                if (temp != null) {
                    switch (temp) {
                        case "boss":
                            dispose();
                            new BossGUI("Boss Menu", socket);
                            break;
                        case "manager":
                            dispose();
                            new ManagerGUI("Manager Menu", socket);
                            break;
                        case "cook":
                            dispose();
                            new CookGUI("Cook Menu", socket);
                            break;
                        case "waiter":
                            dispose();
                            new WaiterGUI("Waiter Menu", socket);
                            break;
                        case "invalidLogin":
                            JOptionPane.showMessageDialog(null, "Invalid login");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Unknown role: " + temp);
                            break;
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR");
            }

        }
    }
}