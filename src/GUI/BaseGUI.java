package GUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public abstract class BaseGUI extends JFrame {
    protected Socket socket;
    protected PrintWriter out;
    protected BufferedReader in;

    public BaseGUI(String title,Socket socket) {
        super(title);
        this.socket = socket;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    protected abstract void init();

    protected abstract void close();




}
