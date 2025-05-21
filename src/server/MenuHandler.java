package server;

import GUI.BossGUI;
import GUI.CookGUI;
import GUI.ManagerGUI;
import GUI.WaiterGUI;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class MenuHandler extends Thread {
    private final Socket socket;

    BufferedReader in;
    PrintWriter out;

    public MenuHandler(Socket Socket) {
        this.socket = Socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            String chooseMenu = in.readLine();
            switch (chooseMenu) {
                case "boss":
                    out.println("Boss Menu has been opened!");
                    out.println("chosen");
                    new BossGUI("BossGUI");
                    break;
                case "manager":
                    out.println("Manager Menu has been opened!");
                    out.println("chosen");
                    new ManagerGUI("ManagerGUI");
                    break;
                case "waiter":
                    out.println("Waiter Menu has been opened!");
                    out.println("chosen");
                    new WaiterGUI("WaiterGUI");
                    break;
                case "cook":
                    out.println("Cook Menu has been opened!");
                    out.println("chosen");
                    new CookGUI("CookGUI");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid login: " + chooseMenu);
                    out.println("chooen");
                    break;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading input: " + e.getMessage());
        }

    }
}