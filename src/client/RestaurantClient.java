package client;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;


public class RestaurantClient {
    public static final int serverPort = 8888;
    public static final String serverIP = "localhost";
    private Socket socket;
    public RestaurantClient()
    {
        try {
            socket = new Socket(serverIP,serverPort);
            new ClientGUI("Restaurant Management Application");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Could not connect to server");
        }
    }
    public static void main(String[] args) {
        new RestaurantClient();
    }
}
