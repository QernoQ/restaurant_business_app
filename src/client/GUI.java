package client;
import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    public GUI(String title) { super(title); }

    void init() {
        setSize(350, 200);
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

        JTextField loginField = new JTextField();
        loginField.setMaximumSize(new Dimension(150, 25));
        loginField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JButton loginButton = new JButton("Enter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(100, 30));

        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(titleLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        loginPanel.add(loginLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalGlue());

        loginContainer.add(loginPanel, BorderLayout.CENTER);
        setVisible(true);
    }

}