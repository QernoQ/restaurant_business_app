package server;


import model.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PositionHandler extends BaseHandler {
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private SaveToFile saveToFile;
    private ReadFromFile readFromFile;

    public PositionHandler(Socket socket, ServerGUI serverGUI) {
        super(socket, serverGUI);
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());
            saveToFile = new SaveToFile(socket, serverGUI);
            readFromFile = new ReadFromFile(socket, serverGUI);


        } catch (IOException e) {
            serverGUI.displayMessage("[POSITION HANDLER] " + e.getMessage());
        }
    }

    public void run() {
        try {
            while (true) {
                Object obj = objectIn.readObject();
                String temp = readFromFile.readID();
                int newID = Integer.parseInt(temp);
                objectOut.writeObject(temp);
                if (obj instanceof Person) {
                    ((Person) obj).setID(newID);
                }
                if (obj instanceof Boss) {
                    saveToFile.saveObjectToFile((Boss) obj);
                } else if (obj instanceof Manager) {
                    saveToFile.saveObjectToFile((Manager) obj);
                } else if (obj instanceof Cook) {
                    saveToFile.saveObjectToFile((Cook) obj);
                } else if (obj instanceof Waiter) {
                    saveToFile.saveObjectToFile((Waiter) obj);
                } else {
                    serverGUI.displayMessage("Unknown object received!");
                }
                saveToFile.saveID(String.valueOf(newID + 1));
            }

        } catch (IOException e) {
            serverGUI.displayMessage("[POSITION HANDLER] Client disconnected: " + socket.getRemoteSocketAddress());
        } catch (ClassNotFoundException e) {
            serverGUI.displayMessage("[POSITION HANDLER] Unknown object type received from client " + socket.getRemoteSocketAddress());
        } finally {
            try {
                if (objectIn != null) {
                    objectIn.close();
                }
                if (objectOut != null) {
                    objectOut.close();
                }
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                serverGUI.displayMessage("[POSITION HANDLER] Error closing resources: " + e.getMessage());
            }
        }

    }
}

