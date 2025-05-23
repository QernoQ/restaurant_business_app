package server;


import model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PersonHandler extends BaseHandler {
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private SaveToFile saveToFile;
    private ReadFromFile readFromFile;

    public PersonHandler(Socket socket, ServerGUI serverGUI) {
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
                Object msg = objectIn.readObject();

                if (!(msg instanceof String)) {
                    serverGUI.displayMessage("[PERSON HANDLER] Invalid sort type received.");
                    continue;
                }

                String sort = (String) msg;

                if (sort.equals("ADD")) {
                    Object obj = objectIn.readObject();

                    if (!(obj instanceof Person)) {
                        serverGUI.displayMessage("[PERSON HANDLER] Received non-person object.");
                        continue;
                    }

                    String temp = readFromFile.readID();
                    int newID = Integer.parseInt(temp);
                    objectOut.writeObject(temp);

                    ((Person) obj).setID(newID);
                    saveToFile.saveObjectToFile(obj);
                    saveToFile.saveID(String.valueOf(newID + 1));

                    serverGUI.displayMessage("[PERSON HANDLER] Added person: " + obj);

                } else if (sort.equals("EDIT")) {
                    ArrayList<Person> temp = readFromFile.readObjectsFromFile("Workers.ser");
                }
            }

        } catch (IOException e) {
            serverGUI.displayMessage("[POSITION HANDLER] Client disconnected: " + socket.getRemoteSocketAddress());
        } catch (ClassNotFoundException e) {
            serverGUI.displayMessage("[POSITION HANDLER] Unknown object type received from client " + socket.getRemoteSocketAddress());
        } finally {
            try {
                if (objectIn != null) objectIn.close();
                if (objectOut != null) objectOut.close();
                if (!socket.isClosed()) socket.close();
            } catch (IOException e) {
                serverGUI.displayMessage("[POSITION HANDLER] Error closing resources: " + e.getMessage());
            }
        }
    }
}


