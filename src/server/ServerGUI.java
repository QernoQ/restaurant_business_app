package server;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerGUI extends JFrame {
    private JTextArea serverArea;
    private DateTimeFormatter formated = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss");

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
        serverArea.setFocusable(true);
        serverArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(serverArea, BorderLayout.CENTER);
        JScrollPane js = new JScrollPane(serverArea);
        add(js, BorderLayout.CENTER);
        setVisible(true);
    }

    public void displayMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        serverArea.append("[" + formated.format(now) + "] " + message + "\n");

    }

}
