package server;

import model.Person;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

/**
 * The `SaveToFile` class provides utility methods for persisting data to files on the server.
 * It handles saving individual objects, lists of objects, and unique ID counters.
 * This class is crucial for maintaining the state of workers and bills across server restarts.
 */
public class SaveToFile implements Serializable {

    /**
     * The socket associated with the client connection. While declared,
     * this field is not directly used within the `SaveToFile` methods,
     * suggesting it might be for contextual information or future extensions.
     */
    private final Socket socket;
    /**
     * A reference to the `ServerGUI` instance, used for displaying messages and
     * logging the status of file save operations.
     */
    private final ServerGUI serverGui;


    /**
     * Constructs a new `SaveToFile` utility object.
     *
     * @param socket    The `Socket` instance associated with the client.
     * @param serverGui A reference to the main `ServerGUI` for logging purposes.
     */
    public SaveToFile(Socket socket, ServerGUI serverGui) {
        this.socket = socket;
        this.serverGui = serverGui;
    }

    /**
     * Saves a new ID (as a string) to a specified text file.
     * This method is typically used to update sequential ID counters for entities like workers or bills.
     * It overwrites the existing content of the file with the new ID.
     *
     * @param newID    The string representation of the new ID to save.
     * @param fileName The path to the file where the ID will be saved (e.g., "id.txt", "billid.txt").
     */
    public void saveID(String newID,String fileName) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(newID);
            serverGui.displayMessage("[SAVETOFILE] ID Changed!");
        } catch (FileNotFoundException e) {
            serverGui.displayMessage("[SAVETOFILE] Cannot create or open file id.txt");
        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error writing to id.txt: " + e.getMessage());
        }
    }

    /**
     * Saves a list of objects to a specified file.
     * Each object in the list is written sequentially to the file using `ObjectOutputStream`.
     * This method overwrites any existing content in the file, effectively saving the entire list.
     *
     * @param personList The `List` of objects to be saved. The type parameter `<?>` indicates it can be a list of any object type.
     * @param fileName   The path to the file where the list will be saved (e.g., "Workers.ser", "bills.ser").
     */
    public void saveListToFile(List<?> personList,String fileName) {
        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Object p : personList) {
                oos.writeObject(p);
            }
            serverGui.displayMessage("[SAVETOFILE] Successfully saved to file!");

        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error saving object: " + e.getMessage());
        }
    }

    /**
     * Saves a single object to a specified file.
     * If the file already exists, the object is appended to the file.
     * If the file does not exist, a new file is created and the object is written.
     * This method uses `AppendableObjectOutputStream` to correctly append objects
     * without writing a new stream header, which would corrupt the file for subsequent deserialization.
     *
     * @param worker   The object to be saved.
     * @param fileName The path to the file where the object will be saved.
     */
    public void saveObjectToFile(Object worker,String fileName) {
        File file = new File(fileName);
        boolean append = file.exists();

        try (FileOutputStream fos = new FileOutputStream(file, true);
             ObjectOutputStream save = append ? new AppendableObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            save.writeObject(worker);
            serverGui.displayMessage("[SAVETOFILE] Successfully saved to file: " + worker.toString());
        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error saving object: " + e.getMessage());
        }
    }

    /**
     * A custom `ObjectOutputStream` that overrides `writeStreamHeader()`.
     * This is necessary when appending objects to an existing object output stream
     * to prevent writing multiple stream headers, which would lead to `StreamCorruptedException`
     * when attempting to deserialize the objects later.
     * By calling `reset()`, it ensures that only the object data is appended.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        /**
         * Constructs an `AppendableObjectOutputStream` that writes to the specified `OutputStream`.
         *
         * @param out The `OutputStream` to which the objects will be written.
         * @throws IOException If an I/O error occurs while writing the stream header.
         */
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        /**
         * Overrides the default `writeStreamHeader()` method to prevent writing a new stream header.
         * Instead, it calls `reset()`, which clears the stream's internal state, allowing
         * objects to be appended correctly to an existing stream without corruption.
         *
         * @throws IOException If an I/O error occurs.
         */
        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}