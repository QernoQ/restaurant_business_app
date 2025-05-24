package GUI;

import GUI.boss.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class BossGUI extends BaseGUI implements ActionListener {
    JButton AddWorker, ManageWorker;
    protected ObjectOutputStream objectOut;
    protected ObjectInputStream objectIn;

    public BossGUI(String title, Socket socket) throws IOException {
        super(title, socket);
        this.objectOut = new ObjectOutputStream(socket.getOutputStream());
        this.objectIn = new ObjectInputStream(socket.getInputStream());
        init();
    }

    @Override
    protected void init() {
        Container BossContainer = getContentPane();
        BossContainer.setLayout(new BorderLayout());
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel titleLabel = new JLabel("Boss Menu");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.DARK_GRAY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        AddWorker = new JButton("Add Worker");
        ManageWorker = new JButton("Manage Workers");

        Font buttonFont = new Font("Serif", Font.BOLD, 18);
        AddWorker.setFont(buttonFont);
        ManageWorker.setFont(buttonFont);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(AddWorker);
        buttonPanel.add(ManageWorker);
        AddWorker.addActionListener(this);
        ManageWorker.addActionListener(this);

        BossContainer.add(titlePanel, BorderLayout.NORTH);
        BossContainer.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == AddWorker) {
            new AddWorkerWindow(this, objectOut);

        } else if (source == ManageWorker) {
            new ManageWorkerWindow(this, objectOut, objectIn);
        }
    }
}

