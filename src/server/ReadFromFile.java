package server;

import java.io.*;
import java.net.Socket;

public class ReadFromFile {
    private final Socket socket;
    private final ServerGUI serverGui;

    public ReadFromFile(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGui = serverGUI;
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
