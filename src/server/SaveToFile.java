package server;

import java.io.*;
import java.net.Socket;

public class SaveToFile implements Serializable {

    private ObjectInputStream objectIn;
    private final Socket socket;
    private final ServerGUI serverGui;

    public SaveToFile(Socket socket, ServerGUI serverGUI) throws IOException {
        this.socket = socket;
        this.serverGui = serverGUI;
    }

    public void saveObjectToFile(Object worker) {
        try (ObjectOutputStream saveObject = new ObjectOutputStream(new FileOutputStream("Workers.ser",true))){
            saveObject.writeObject(worker);
            serverGui.displayMessage("Successfully saved to file: " + worker.toString());
        } catch(IOException e){
            serverGui.displayMessage("Error saving object: " + e.toString());
        }

    }
}
