package server;

import model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends BaseHandler {
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private SaveToFile saveToFile;
    private ReadFromFile readFromFile;
    private Bill bill;
    private static final Map<Integer, Boolean> lockedBills = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket, ServerGUI serverGUI) {
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


                List<Person> allPersons = readFromFile.readObjectsFromFile("Workers.ser")
                        .stream()
                        .filter(obj -> obj instanceof Person)
                        .map(obj -> (Person) obj)
                        .toList();
                List<Boss> bosses = new ArrayList<>();
                List<Cook> cooks = new ArrayList<>();
                List<Waiter> waiters = new ArrayList<>();
                List<Bill> readBill = readFromFile.readObjectsFromFile("bills.ser")
                        .stream()
                        .filter(obj -> obj instanceof Bill)
                        .map(obj -> (Bill) obj)
                        .toList();

                for (Object p : allPersons) {
                    if (p instanceof Boss b) bosses.add(b);
                    else if (p instanceof Cook c) cooks.add(c);
                    else if (p instanceof Waiter w) waiters.add(w);
                }

                switch (sort) {
                    case ADD -> {
                        Object obj = objectIn.readObject();
                        String temp = readFromFile.readID("id.txt");
                        int newID = Integer.parseInt(temp);
                        ((Person) obj).setID(newID);
                        saveToFile.saveObjectToFile(obj,"Workers.ser");
                        saveToFile.saveID(String.valueOf(newID + 1),"id.txt");
                    }
                    case BOSS -> {
                        objectOut.writeObject(bosses);
                        objectOut.flush();
                        for (Boss b : bosses) {
                            serverGUI.displayMessage("Loaded Boss list " + b);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case COOK -> {
                        objectOut.writeObject(cooks);
                        objectOut.flush();
                        for (Cook c : cooks) {
                            serverGUI.displayMessage("Loaded Cook list " + c);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case WAITER -> {
                        objectOut.writeObject(waiters);
                        objectOut.flush();
                        for (Waiter w : waiters) {
                            serverGUI.displayMessage("Loaded Waiter list " + w);
                        }
                        serverGUI.displayMessage("-------------");
                    }
                    case SAVE -> {
                        serverGUI.displayMessage("[PERSON HANDLER] Changed Worker from: " + objectIn.readObject() + "to: " + objectIn.readObject());
                        List<Person> persons = (List<Person>) objectIn.readObject();
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
                        saveToFile.saveListToFile(persons,"Workers.ser");
                    }
                    case REMOVE -> {
                        Person delete = (Person) objectIn.readObject();
                        serverGUI.displayMessage("[PERSON HANDLER] Removing " + delete);
                        allPersons.removeIf(p -> p.getId() == delete.getId());
                        allPersons.sort((p1, p2) -> Integer.compare(p1.getId(), p2.getId()));
                        saveToFile.saveListToFile(allPersons,"Workers.ser");
                        String temp = readFromFile.readID("id.txt");
                        int newID = Integer.parseInt(temp);
                        if (newID <= 0) {
                            newID = 0;
                        } else {
                            newID = newID - 1;
                        }
                        saveToFile.saveID(String.valueOf(newID),"id.txt");
                    }
                    case ADDBILL ->
                    {
                        String temp = readFromFile.readID("billid.txt");
                        int currentId = Integer.parseInt(temp);
                        int nextID = currentId + 1;
                        objectOut.writeObject(nextID);
                        bill = (Bill) objectIn.readObject();
                        serverGUI.displayMessage("[PERSON HANDLER] Added bill:  " + bill);
                        saveToFile.saveID(String.valueOf(nextID),"billid.txt");
                        saveToFile.saveObjectToFile(bill,"bills.ser");
                    }
                    case READBILL ->
                    {
                        serverGUI.displayMessage("[PERSON HANDLER] Reading Bill");
                        objectOut.writeObject(readBill);
                        objectOut.flush();
                    }
                    case CLOSEBILL ->
                    {
                        serverGUI.displayMessage("[PERSON HANDLER] CLOSEBILL received from client.");
                        bill = (Bill) objectIn.readObject();
                        saveBill(readBill);
                        objectOut.writeObject(SortEnum.BILL_CLOSED);
                        objectOut.flush();
                        serverGUI.displayMessage("[PERSON HANDLER] Closing Bill!");
                        int billId = (int) objectIn.readInt();
                        lockedBills.remove(billId);
                        serverGUI.displayMessage(billId + " removed from map");
                    }
                    case BACKBILL ->
                    {
                        serverGUI.displayMessage("[PERSON HANDLER] BACKBILL received from client.");
                        bill = (Bill) objectIn.readObject();
                        saveBill(readBill);
                        objectOut.writeObject(SortEnum.BILL_CLOSED);
                        objectOut.flush();
                        serverGUI.displayMessage("[PERSON HANDLER] User go back to all bills (bill not closed)!");
                        int billId = (int) objectIn.readInt();
                        lockedBills.remove(billId);
                        serverGUI.displayMessage("Bill: " + billId  + " has been unlocked.");
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
    public void saveBill(List<Bill> bills) {
        List<Bill> modifiableBills = new ArrayList<>(bills);
        modifiableBills.removeIf(b -> b.getBillId() == bill.getBillId());
        modifiableBills.add(bill);
        modifiableBills.sort((b1, b2) -> Integer.compare(b1.getBillId(), b2.getBillId()));
        saveToFile.saveListToFile(modifiableBills,"bills.ser");
        serverGUI.displayMessage("[PERSON HANDLER] Bill save succesful!");
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
            case "TRY_LOCK_BILL" -> {
                int billId = (int) objectIn.readInt();
                if(lockedBills.getOrDefault(billId,false)){
                    objectOut.writeObject(SortEnum.BILL_ALREADY_LOCKED);
                    serverGUI.displayMessage("Bill: " + billId  +" already locked.");
                } else {
                    lockedBills.put(billId,true);
                    objectOut.writeObject(SortEnum.BILL_LOCK_SUCCESS);
                    serverGUI.displayMessage("Bill: " + billId + " has been locked.");
                }
            }
            default -> serverGUI.displayMessage("[PERSON HANDLER] Unknown command: " + cmd);
        }
        objectOut.flush();
    }
}
