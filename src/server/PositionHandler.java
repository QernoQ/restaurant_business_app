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

    public PositionHandler(Socket socket,ServerGUI serverGUI) {
        super(socket, serverGUI);
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());
            saveToFile = new SaveToFile(socket,serverGUI);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

        public void run() {
        try {
            while (true) {
                Object obj = objectIn.readObject();
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
            }

        } catch (IOException | ClassNotFoundException e) {
            serverGUI.displayMessage(e.getMessage());
            }


        }
    }

