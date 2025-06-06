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

/**
 * The `ServerGUI` class provides a graphical user interface for the server application.
 * It displays real-time logs and messages generated by the server, offering a visual
 * representation of server activity and operations. All displayed messages are also
 * saved to a log file.
 */
/**
 * @author Patryk Bocheński
 */
public class ServerGUI extends JFrame {
    /**
     * The `JTextArea` component used to display server messages and logs.
     */
    private JTextArea serverArea;
    /**
     * The `DateTimeFormatter` used to format timestamps for log entries.
     * The format is "dd-MM-yyyy - HH:mm:ss".
     */
    private DateTimeFormatter formated = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss");

    /**
     * @deprecated This `PrintWriter` instance is declared but not initialized or used within the class.
     * It might be a remnant from previous development or intended for future functionality.
     */
    PrintWriter in;

    /**
     * Constructs a new `ServerGUI` window with the specified title.
     *
     * @param title The title to be displayed in the window's title bar.
     */
    public ServerGUI(String title) {
        super(title);
        init();
    }

    /**
     * Initializes the components and layout of the `ServerGUI` window.
     * Sets up the `JTextArea` for displaying messages, configures its appearance
     * (background, foreground, font, line wrapping), makes it read-only, and
     * ensures the scroll pane automatically updates to show the latest messages.
     */
    public void init() {
        setLocationRelativeTo(null);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        serverArea = new JTextArea();
        serverArea.setBackground(new Color(100,100,100));
        serverArea.setForeground(Color.white);
        serverArea.setLineWrap(true);
        serverArea.setWrapStyleWord(true);

        /**
         * Ensures that the scroll pane always scrolls to the bottom when new text is appended,
         * so the latest messages are always visible.
         */
        DefaultCaret caret = (DefaultCaret) serverArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        serverArea.setEditable(false);
        serverArea.setFocusable(true);
        serverArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(serverArea, BorderLayout.CENTER);

        JScrollPane js = new JScrollPane(serverArea);
        add(js, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Displays a given message in the `serverArea` text component and
     * also saves it to a log file. Each message is prepended with a timestamp.
     *
     * @param message The message string to be displayed and logged.
     */
    public void displayMessage(String message) {
        LocalDateTime now = LocalDateTime.now();
        String formatted = "[" + formated.format(now) + "] " + message + "\n";
        serverArea.append(formatted);
        saveLog(formatted);
    }

    /**
     * Saves a given message string to a log file named "logs.txt".
     * Messages are appended to the file, and a new line is added after each entry.
     * If an `IOException` occurs during writing, an error message is displayed
     * in the `serverArea` (but not recursively logged to the file to prevent infinite loops).
     *
     * @param message The message string to be saved to the log file.
     */
    public void saveLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("logs.txt",true))) {
            writer.write(message);
        } catch (IOException e) {
            displayMessage("Error saving log");
        }
    }
}