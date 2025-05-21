package server;

import client.Send;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
