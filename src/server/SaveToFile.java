package server;

import model.Person;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SaveToFile implements Serializable {

    private final Socket socket;
    private final ServerGUI serverGui;


    public SaveToFile(Socket socket, ServerGUI serverGui) {
        this.socket = socket;
        this.serverGui = serverGui;
    }
    public void saveID(String newID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("id.txt"))) {
            writer.write(newID);
            serverGui.displayMessage("[SAVETOFILE] ID Changed!");
        } catch (FileNotFoundException e) {
            serverGui.displayMessage("[SAVETOFILE] Cannot create or open file id.txt");
        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error writing to id.txt: " + e.getMessage());
        }
    }

    public void saveListToFile(ArrayList<Person> personList) {
        File file = new File("Workers.ser");

        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Person p : personList) {
                oos.writeObject(p);
            }
            serverGui.displayMessage("[SAVETOFILE] Successfully saved to file!");

        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error saving object: " + e.getMessage());
        }
    }

    public void saveObjectToFile(Object worker) {
        File file = new File("Workers.ser");
        boolean append = file.exists();

        try (FileOutputStream fos = new FileOutputStream(file, true);
             ObjectOutputStream save = append ? new AppendableObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            save.writeObject(worker);
            serverGui.displayMessage("[SAVETOFILE] Successfully saved to file: " + worker.toString());
        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] Error saving object: " + e.getMessage());
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            reset();
        }
    }
}
