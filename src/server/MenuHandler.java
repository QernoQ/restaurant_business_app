package server;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Locale;

public class MenuHandler extends Thread {
    private final Socket socket;

    private final ServerGUI serverGUI;
    BufferedReader in;
    PrintWriter out;

    public MenuHandler(Socket Socket,ServerGUI serverGUI) {
        this.socket = Socket;
        this.serverGUI = serverGUI;
    }

    public void run() {
        String chooseMenu;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            while (true) {
                chooseMenu = in.readLine();
                if (chooseMenu == null) {
                    break;
                }
            switch (chooseMenu) {
                case "boss":
                    out.println("boss");
                    serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected!");
                    return;
                case "manager":
                    out.println("manager");
                    serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected!");
                    return;
                case "waiter":
                    out.println("waiter");
                    serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected!");
                    return;
                case "cook":
                    out.println("cook");
                    serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected!");
                    return;
                default:
                    out.println("invalidLogin");
                    serverGUI.displayMessage("Invalid login!");
                    break;
            }
        }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading input: " + e.getMessage());
        }

    }
}