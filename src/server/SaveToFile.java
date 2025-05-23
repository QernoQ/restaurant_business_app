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

    public void saveID(String newID)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("id.txt"))) {
            writer.write(newID);
        } catch (FileNotFoundException e) {
            serverGui.displayMessage("[SAVETOFILE] Cannot create or open file id.txt");
        } catch (IOException e) {
            serverGui.displayMessage("[SAVETOFILE] ERROR");
        }
    }

    public void saveObjectToFile(Object worker) {
        try (ObjectOutputStream saveObject = new ObjectOutputStream(new FileOutputStream("Workers.ser",true))){
            saveObject.writeObject(worker);
            serverGui.displayMessage("Successfully saved to file: " + worker.toString());
        } catch(IOException e){
            serverGui.displayMessage("[SAVETOFILE] Error saving object: " + e.toString());
        }

    }
}
