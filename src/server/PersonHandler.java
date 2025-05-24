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
            this.saveToFile = new SaveToFile(socket, serverGUI);
            this.readFromFile = new ReadFromFile(socket, serverGUI);
        } catch (IOException e) {
            serverGUI.displayMessage("[PERSON HANDLER] " + e.getMessage());
        }
    }

    public void run() {
        try {
            while (true) {
                Object msg = objectIn.readObject();

                if (!(msg instanceof String sort)) {
                    serverGUI.displayMessage("[PERSON HANDLER] Invalid sort type received.");
                    continue;
                }

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
                    continue;
                }

                ArrayList<Person> allPersons = readFromFile.readObjectsFromFile("Workers.ser");
                ArrayList<Boss> bosses = new ArrayList<>();
                ArrayList<Manager> managers = new ArrayList<>();
                ArrayList<Cook> cooks = new ArrayList<>();
                ArrayList<Waiter> waiters = new ArrayList<>();

                for (Person p : allPersons) {
                    if (p instanceof Boss b) bosses.add(b);
                    else if (p instanceof Manager m) managers.add(m);
                    else if (p instanceof Cook c) cooks.add(c);
                    else if (p instanceof Waiter w) waiters.add(w);
                }

                switch (sort) {
                    case "BOSS" -> {
                        objectOut.writeObject(bosses);
                        for (Boss b : bosses) {
                            serverGUI.displayMessage("Loaded Boss list " + b);
                        }
                    }
                    case "MANAGER" -> {
                        objectOut.writeObject(managers);
                        for (Manager m : managers) {
                            serverGUI.displayMessage("Loaded Manager list " + m);
                        }
                    }
                    case "COOK" -> {
                        objectOut.writeObject(cooks);
                        for (Cook c : cooks) {
                            serverGUI.displayMessage("Loaded Cook list " + c);
                        }
                    }
                    case "WAITER" -> {
                        objectOut.writeObject(waiters);
                        for (Waiter w : waiters) {
                            serverGUI.displayMessage("Loaded Waiter list " + w);
                        }
                    }
                    default -> serverGUI.displayMessage("[PERSON HANDLER] Unknown sort command: " + sort);
                }

            }
        } catch (IOException e) {
            serverGUI.displayMessage("[PERSON HANDLER] Client disconnected: " + socket.getRemoteSocketAddress());
        } catch (ClassNotFoundException e) {
            serverGUI.displayMessage("[PERSON HANDLER] Unknown object type received from client " + socket.getRemoteSocketAddress());
        } finally {
            try {
                if (objectIn != null) objectIn.close();
                if (objectOut != null) objectOut.close();
                if (!socket.isClosed()) socket.close();
            } catch (IOException e) {
                serverGUI.displayMessage("[PERSON HANDLER] Error closing resources: " + e.getMessage());
            }
        }
    }
}
