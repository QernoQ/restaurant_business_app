package client;

import GUI.BossGUI;
import GUI.ClientGUI;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;


public class RestaurantClient  {
    public static final int serverPort = 8888;
    public static final String serverIP = "localhost";
    private Socket socket;
    public RestaurantClient()
    {
        try {
            socket = new Socket(serverIP,serverPort);
            new ClientGUI("Restaurant Management Application",socket);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Could not connect to server");
        }
    }
    public Socket getSocket() {
        return socket;
    }
    public void start()
    {

        if(this.getSocket() == null) {
            JOptionPane.showMessageDialog(null, "Socket could not be created!");
            return;
        }
        new Send(this.getSocket()).start();
    }
    public static void main(String[] args) {
        RestaurantClient client = new RestaurantClient();
        client.start();

    }
}
