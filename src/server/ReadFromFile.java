package server;

import model.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ReadFromFile implements Serializable {
    private final Socket socket;
    private final ServerGUI serverGui;


    public ReadFromFile(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGui = serverGUI;

    }

    public ArrayList<Person> readObjectsFromFile(String fileName) {
        ArrayList<Person> persons = new ArrayList<Person>();
        try (ObjectInputStream readObject = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    Object obj = readObject.readObject();
                    if (obj instanceof Person) {
                        persons.add((Person) obj);
                    }
                } catch (EOFException eof) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            serverGui.displayMessage("Failed to read file: " + e.toString());
        }
        return persons;
    }

    public String readID() {
        File idFile = new File("id.txt");

        if (!idFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(idFile))) {
                writer.write("1");
            } catch (IOException e) {
                serverGui.displayMessage("[READFROMFILE] Error creating id.txt: " + e.getMessage());
                return null;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(idFile))) {
            return reader.readLine();
        } catch (IOException e) {
            serverGui.displayMessage("[READFROMFILE] Error reading from file id.txt: " + e.getMessage());
            return null;
        }
    }
}
