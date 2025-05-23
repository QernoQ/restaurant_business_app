package server;


import model.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PositionHandler extends BaseHandler {
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    ArrayList<Boss> bossList = new ArrayList<>();
    ArrayList<Manager> managerList = new ArrayList<>();
    ArrayList<Cook> cookList = new ArrayList<>();
    ArrayList<Waiter> waiterList = new ArrayList<>();


    public PositionHandler(Socket socket,ServerGUI serverGUI) {
        super(socket, serverGUI);
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

        public void run() {
        try {
            while (true) {
                Object obj = objectIn.readObject();
                if (obj instanceof Boss) {
                    bossList.add((Boss) obj);
                    serverGUI.displayMessage("Added Boss to system: " + obj.toString());
                } else if (obj instanceof Manager) {
                    managerList.add((Manager) obj);
                    serverGUI.displayMessage("Added Manager to system: " + obj.toString());
                } else if (obj instanceof Cook) {
                    cookList.add((Cook) obj);
                    serverGUI.displayMessage("Added Cook to system: " + obj.toString());
                } else if (obj instanceof Waiter) {
                    waiterList.add((Waiter) obj);
                    serverGUI.displayMessage("Added Waiter to system: " + obj.toString());
                } else {
                    serverGUI.displayMessage("Unknown object received!");
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            serverGUI.displayMessage(e.getMessage());
            }


        }
    }

