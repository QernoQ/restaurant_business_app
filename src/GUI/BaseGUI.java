package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
/**
 * @author Patryk Bocheński
 */
/**
 * The `BaseGUI` abstract class serves as a foundational class for all GUI windows in the application.
 * It provides common properties and methods for network communication (sockets, object streams)
 * and consistent styling for Swing components.
 */
public abstract class BaseGUI extends JFrame {
    /**
     * The socket connected to the server.
     */
    protected Socket socket;
    /**
     * A `PrintWriter` for sending text data to the server.
     * @deprecated Consider using `ObjectOutputStream` for all communication for consistency.
     */
    protected PrintWriter out;
    /**
     * A `BufferedReader` for reading text data from the server.
     * @deprecated Consider using `ObjectInputStream` for all communication for consistency.
     */
    protected BufferedReader in;
    /**
     * Default font for labels in the GUI.
     */
    public Font labelFont = new Font("Serif", Font.BOLD, 14);
    /**
     * Default font for input fields in the GUI.
     */
    public Font inputFont = new Font("Serif", Font.PLAIN, 14);
    /**
     * Default foreground color for labels.
     */
    public Color labelColor = Color.WHITE;
    /**
     * Default background color for input fields.
     */
    public Color inputBg = new Color(60, 60, 60);
    /**
     * Default background color for labels.
     */
    public Color labelBg = new Color(100, 100, 100);

    /**
     * `ObjectOutputStream` for sending serialized Java objects to the server.
     */
    protected ObjectOutputStream objectOut;
    /**
     * `ObjectInputStream` for receiving serialized Java objects from the server.
     */
    protected ObjectInputStream objectIn;

    /**
     * The port number for the server connection.
     */
    public static final int serverPort = 8888;
    /**
     * The IP address or hostname of the server.
     */
    public static final String serverIP = "localhost";



    /**
     * Constructs a new `BaseGUI` window.
     * Initializes the window title and sets up object input/output streams for communication
     * with the server via the provided socket.
     *
     * @param title  The title to be displayed in the window's title bar.
     * @param socket The `Socket` object used for network communication with the server.
     */
    public BaseGUI(String title,Socket socket) {
        super(title);
        this.socket = socket;
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Applies a consistent style to a `JButton`.
     * This includes setting background, foreground, font, border, and other visual properties.
     *
     * @param button The `JButton` to be styled.
     */
    public void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setOpaque(true);
    }
    /**
     * Applies a consistent style to a generic Swing `JComponent`.
     * This includes setting the font, foreground color, and background color.
     *
     * @param component The `JComponent` to be styled.
     * @param font      The `Font` to apply to the component.
     * @param colorF    The foreground `Color` to apply to the component.
     * @param colorB    The background `Color` to apply to the component.
     */
    public void style(JComponent component, Font font, Color colorF,Color colorB) {
        component.setFont(font);
        component.setForeground(colorF);
        component.setBackground(colorB);
    }
    /**
     * Abstract method to be implemented by subclasses for initializing their specific GUI components and layout.
     */
    protected abstract void init();


}