package server;

import model.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The `ReadFromFile` class provides utility methods for reading serialized objects
 * and simple ID values from files on the server side. It's used by handlers
 * to retrieve persistent data like worker lists, bills, and unique ID counters.
 */
public class ReadFromFile implements Serializable {
    /**
     * The socket associated with the client connection. This field is declared
     * but not directly used within the `ReadFromFile` methods themselves,
     * suggesting it might be for context or potential future use in the handler.
     */
    private final Socket socket;
    /**
     * A reference to the `ServerGUI` instance for displaying messages and logging
     * file operation statuses or errors.
     */
    private final ServerGUI serverGui;


    /**
     * Constructs a new `ReadFromFile` utility object.
     *
     * @param socket    The `Socket` instance associated with the client.
     * @param serverGUI A reference to the main `ServerGUI` for logging purposes.
     */
    public ReadFromFile(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGui = serverGUI;

    }

    /**
     * Reads a list of serialized objects from a specified file.
     * This method is designed to read all objects that have been
     * sequentially written to an object output stream in a given file.
     *
     * @param fileName The path to the file from which to read the objects.
     * @return A `List` of `Object` containing all deserialized objects found in the file.
     * Returns an empty list if the file does not exist or if an error occurs during reading.
     */
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

    /**
     * Reads a single integer ID from a specified text file.
     * If the file does not exist, it creates the file and initializes it with "0".
     * This is typically used for managing sequential IDs for new entities (e.g., workers, bills).
     *
     * @param fileName The path to the file containing the ID.
     * @return The ID read from the file as a `String`, or `null` if an error occurs.
     */
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