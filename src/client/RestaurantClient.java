package client;

import GUI.ClientGUI;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
/**
 * @author Patryk Bocheński
 */

/**
 * The {@code RestaurantClient} class represents the client-side component
 * of a restaurant management application. It attempts to connect to a server
 * via a socket and launches a graphical user interface for the client.
 */
public class RestaurantClient {

    /**
     * The port number used to connect to the server.
     */
    public static final int serverPort = 8888;

    /**
     * The IP address of the server.
     */
    public static final String serverIP = "localhost";

    /**
     * The socket used for communication with the server.
     */
    private Socket socket;

    /**
     * Constructs a {@code RestaurantClient} object and attempts to connect
     * to the server. If the connection is successful, the client GUI is launched.
     * If the connection fails, a dialog box is shown to the user.
     */
    public RestaurantClient() {
        try {
            socket = new Socket(serverIP, serverPort);
            new ClientGUI("Restaurant Management Application", socket);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to server");
        }
    }
    public Socket getSocket() {
        return socket;
    }

    /**
     * Starts the client session. Displays an error message if the socket is not created.
     */
    public void start() {
        if (this.getSocket() == null) {
            JOptionPane.showMessageDialog(null, "Socket could not be created!");
            return;
        }
    }

    /**
     * The main method to run the {@code RestaurantClient}. It creates an instance of the client
     * and starts the session.
     */
    public static void main(String[] args) {
        RestaurantClient client = new RestaurantClient();
        client.start();
    }
}
