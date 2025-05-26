package server;

import model.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReadFromFile implements Serializable {
    private final Socket socket;
    private final ServerGUI serverGui;


    public ReadFromFile(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGui = serverGUI;

    }

    public List<Object> readObjectsFromFile(String fileName) {
        List<Object> currentList = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            serverGui.displayMessage("[READFROMFILE] File not found: " + fileName);
            return currentList;
        }
        try (ObjectInputStream readObject = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    Object obj = readObject.readObject();
                        currentList.add(obj);
                } catch (EOFException eof) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            serverGui.displayMessage("Failed to read file: " + e.toString());
        }
        return currentList;
    }

    public String readID(String fileName) {
        File idFile = new File(fileName);

        if (!idFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(idFile))) {
                writer.write("0");
            } catch (IOException e) {
                serverGui.displayMessage("[READFROMFILE] Error creating:  " + idFile + e.getMessage());
                return null;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(idFile))) {
            return reader.readLine();
        } catch (IOException e) {
            serverGui.displayMessage("[READFROMFILE] Error reading from file: " + idFile + e.getMessage());
            return null;
        }
    }
}
