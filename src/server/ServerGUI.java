package server;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        DefaultCaret caret = (DefaultCaret) serverArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
        String formatted = "[" + formated.format(now) + "] " + message + "\n";
        serverArea.append(formatted);
        saveLog(formatted);
    }
    public void saveLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt",true))) {
            writer.write(message);
        } catch (IOException e) {
            displayMessage("Error saving log");
        }
    }


}
