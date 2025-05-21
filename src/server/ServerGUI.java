package server;

import javax.swing.*;
import java.awt.*;

public class ServerGUI extends JFrame {
    private JTextArea serverArea;
    public ServerGUI(String title) {
        super(title);
        init();
    }
    public void init() {
        setLocationRelativeTo(null);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        serverArea = new JTextArea();
        serverArea.setEditable(false);
        serverArea.setFocusable(false);
        add(serverArea, BorderLayout.CENTER);
        JScrollPane js = new JScrollPane(serverArea);
        add(js, BorderLayout.CENTER);
        setVisible(true);

    }

    }
