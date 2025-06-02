package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class ClientGUI extends BaseGUI implements ActionListener {
    private JButton loginButton,test1,test2;
    private JTextField loginTextField;

    public ClientGUI(String title, Socket socket) {
        super(title, socket);
        init();
    }

    public void init() {
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Container loginContainer = getContentPane();
        loginContainer.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(40, 40, 40));
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        Font titleFont = new Font("Serif", Font.BOLD, 18);
        Font labelFont = new Font("sSerif", Font.PLAIN, 14);

        JLabel titleLabel = new JLabel("🔒 Log in to access");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(labelFont);
        loginLabel.setForeground(Color.LIGHT_GRAY);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        loginTextField = new JTextField();
        loginTextField.setMaximumSize(new Dimension(200, 30));
        loginTextField.setFont(new Font("Serif", Font.PLAIN, 14));
        loginTextField.setHorizontalAlignment(JTextField.CENTER);
        loginTextField.setBackground(new Color(60, 60, 60));
        loginTextField.setForeground(Color.WHITE);
        loginTextField.setCaretColor(Color.WHITE);
        loginTextField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));

        loginButton = new JButton("Login");
        loginButton.setFont(labelFont);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginPanel.add(titleLabel);
        loginPanel.add(loginLabel);
        loginPanel.add(loginTextField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        loginPanel.add(loginButton);

        loginButton.addActionListener(this);
        loginContainer.add(loginPanel, BorderLayout.CENTER);

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
        getRootPane().getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

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