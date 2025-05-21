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
    PrintWriter out;
    BufferedReader in;

    private final Socket socket;

    public ClientGUI(String title,Socket socket) {
        super(title);
        this.socket = socket;
        init();
    }

    public void init() {
        try {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"ERROR");
        }
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
                if(in.readLine().equals("chosen"))
                {
                    dispose();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"ERROR");
            }

        }
    }
}