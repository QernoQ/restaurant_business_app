package server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class DataBase extends Thread {

    private final Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    public DataBase(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
}
