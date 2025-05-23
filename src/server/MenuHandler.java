package server;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class MenuHandler extends BaseHandler {
    private BufferedReader in;
    private PrintWriter out;

    public MenuHandler(Socket socket, ServerGUI serverGUI) {
        super(socket, serverGUI);
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String chooseMenu;

            while ((chooseMenu = in.readLine()) != null) {
                chooseMenu = chooseMenu.toLowerCase();
                switch (chooseMenu) {
                    case "boss":
                    case "manager":
                    case "waiter":
                    case "cook":
                        out.println(chooseMenu);
                        serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected by : " + socket.getInetAddress().getHostAddress());
                        new PositionHandler(socket, serverGUI).start();
                        return;
                    default:
                        out.println("invalidLogin");
                        serverGUI.displayMessage("Invalid login!");
                        break;
                }
            }

        } catch (IOException e) {
            serverGUI.displayMessage("[MENU HANDLER] " + e.getMessage());
        }
    }
}

