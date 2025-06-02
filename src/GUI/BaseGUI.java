package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public abstract class BaseGUI extends JFrame {
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;
    public Font labelFont = new Font("Serif", Font.BOLD, 14);
    public Font inputFont = new Font("Serif", Font.PLAIN, 14);
    public Color labelColor = Color.WHITE;
    public Color inputBg = new Color(60, 60, 60);
    public Color labelBg = new Color(100, 100, 100);

    protected ObjectOutputStream objectOut;
    protected ObjectInputStream objectIn;

    public static final int serverPort = 8888;
    public static final String serverIP = "localhost";



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
    public void styleButton(JButton button) {
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setOpaque(true);
    }
    public void style(JComponent component, Font font, Color colorF,Color colorB) {
        component.setFont(font);
        component.setForeground(colorF);
        component.setBackground(colorB);
    }
    protected abstract void init();


}
