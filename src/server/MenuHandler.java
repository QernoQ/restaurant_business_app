package server;

import java.io.*;
import java.net.Socket;

public class MenuHandler extends BaseHandler {
    protected ObjectOutputStream objectOut;
    protected ObjectInputStream objectIn;

    public MenuHandler(Socket socket, ServerGUI serverGUI) {
        super(socket, serverGUI);
    }

    public void run() {
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());

            String chooseMenu;

            while ((chooseMenu = (String) objectIn.readObject()) != null) {
                chooseMenu = chooseMenu.toLowerCase();
                switch (chooseMenu) {
                    case "boss":
                    case "manager":
                    case "waiter":
                    case "cook":
                        objectOut.writeObject(chooseMenu);
                        serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected by : " + socket.getInetAddress().getHostAddress());
                        new ClientHandler(socket, serverGUI).start();
                        return;
                    default:
                        objectOut.writeObject("invalidLogin");
                        serverGUI.displayMessage("Invalid login!");
                        break;
                }
            }

        } catch (IOException e) {
            serverGUI.displayMessage("[MENU HANDLER] " + e.getMessage());
        } catch (ClassNotFoundException e) {
            serverGUI.displayMessage("[MENU HANDLER] " + e.getMessage());
        }
    }
}

