package server;

import model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                Object input = objectIn.readObject();

                if (input instanceof String cmd) {
                    handleStringCommand(cmd);
                    continue;
                }
                SortEnum sort;
                if (input instanceof SortEnum s) {
                    sort = s;
                } else {
                    serverGUI.displayMessage("[PERSON HANDLER] Unknown request type from client.");
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
                    case ADD -> {
                        Object obj = objectIn.readObject();
                        String temp = readFromFile.readID();
                        int newID = Integer.parseInt(temp);
                        ((Person) obj).setID(newID);
                        saveToFile.saveObjectToFile(obj);
                        saveToFile.saveID(String.valueOf(newID + 1));
                    }
                    case BOSS -> {
                        objectOut.writeObject(bosses);
                        for (Boss b : bosses) {
                            serverGUI.displayMessage("Loaded Boss list " + b);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case MANAGER -> {
                        objectOut.writeObject(managers);
                        for (Manager m : managers) {
                            serverGUI.displayMessage("Loaded Manager list " + m);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case COOK -> {
                        objectOut.writeObject(cooks);
                        for (Cook c : cooks) {
                            serverGUI.displayMessage("Loaded Cook list " + c);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case WAITER -> {
                        objectOut.writeObject(waiters);
                        for (Waiter w : waiters) {
                            serverGUI.displayMessage("Loaded Waiter list " + w);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case SAVE -> {
                        serverGUI.displayMessage("[PERSON HANDLER] Changed Worker from: " + objectIn.readObject() + "to: " + objectIn.readObject());
                        ArrayList<Person> persons = (ArrayList<Person>) objectIn.readObject();
                        Map<Integer, Person> map = new HashMap<>();
                        for (Person p : persons) {
                            map.put(p.getId(), p);
                        }
                        for (Person p : allPersons) {
                            map.putIfAbsent(p.getId(), p);
                        }
                        persons.clear();
                        persons.addAll(map.values());
                        persons.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
                        saveToFile.saveListToFile(persons);
                    }
                    case REMOVE -> {
                        Person delete = (Person) objectIn.readObject();
                        serverGUI.displayMessage("[PERSON HANDLER] Removing " + delete);
                        allPersons.removeIf(p -> p.getId() == delete.getId());
                        allPersons.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
                        saveToFile.saveListToFile(allPersons);
                        String temp = readFromFile.readID();
                        int newID = Integer.parseInt(temp);
                        if (newID <= 0) {
                            newID = 0;
                        } else {
                            newID = newID - 1;
                        }
                        saveToFile.saveID(String.valueOf(newID));
                    }
                    default -> serverGUI.displayMessage("[PERSON HANDLER] Unknown sort command: " + sort);
                }

            }
        } catch (IOException e) {
            serverGUI.displayMessage("[PERSON HANDLER] Client disconnected: " + socket.getRemoteSocketAddress());
            WindowManager.closeAddWorker();
            WindowManager.closeManageWorker();
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

    private void handleStringCommand(String cmd) throws IOException {
        switch (cmd) {
            case "TRY_OPEN_ADD_WORKER" -> {
                serverGUI.displayMessage("[PERSON HANDLER] Open Add Worker...");
                objectOut.writeBoolean(WindowManager.tryOpenAddWorker());
            }
            case "CLOSE_ADD_WORKER" -> {
                WindowManager.closeAddWorker();
                serverGUI.displayMessage("[PERSON HANDLER] Close Add Worker...");
            }
            case "TRY_OPEN_MANAGE_WORKER" -> {
                objectOut.writeBoolean(WindowManager.tryOpenManageWorker());
                serverGUI.displayMessage("[PERSON HANDLER] Open Manage Worker...");
            }
            case "CLOSE_MANAGE_WORKER" -> {
                WindowManager.closeManageWorker();
                serverGUI.displayMessage("[PERSON HANDLER] Close Manage Worker...");
            }
            default -> serverGUI.displayMessage("[PERSON HANDLER] Unknown command: " + cmd);
        }
        objectOut.flush();
    }
}
