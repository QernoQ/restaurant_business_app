package server;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerGUI extends JFrame {
    private JTextArea serverArea;

    PrintWriter in;

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

    public void displayMessage(String message) {


        serverArea.append(message + "\n");
    }

}
