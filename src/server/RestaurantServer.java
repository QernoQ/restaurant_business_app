package server;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * The `RestaurantServer` class is the main entry point for the server application.
 * It initializes the server socket, sets up the server's graphical user interface,
 * and continuously listens for incoming client connections, delegating each new
 * connection to a `MenuHandler` for initial processing.
 */
public class RestaurantServer {
    /**
     * The port number on which the server listens for incoming client connections.
     */
    public static final int serverPort = 8888;
    /**
     * The `ServerSocket` object responsible for listening for and accepting client connections.
     */
    private ServerSocket serverSocket;
    /**
     * The `ServerGUI` instance used to display server-side messages and logs.
     */
    private ServerGUI serverGUI;

    /**
     * Constructs a new `RestaurantServer`.
     * It attempts to create a `ServerSocket` on the predefined port and
     * initializes the `ServerGUI`. If the port is already in use (meaning
     * another instance of the server is running), it displays an error message
     * and exits the application.
     *
     * @throws IOException If an I/O error occurs when opening the server socket.
     */
    public RestaurantServer() throws IOException {
        try {
            serverSocket = new ServerSocket(serverPort);
            serverGUI = new ServerGUI("Server");
            serverGUI.displayMessage("Server started");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Server already running!");
            System.exit(0);
        }
    }

    /**
     * The main server loop. This method continuously waits for client connections.
     * When a client connects, it accepts the connection, creates a new `MenuHandler`
     * for that client's socket, and starts the handler in a new thread. This allows
     * the server to handle multiple clients concurrently.
     *
     * @throws IOException If an I/O error occurs when accepting a client connection.
     */
    void work() throws IOException {
        /**
         * The server continues to run indefinitely, accepting new client connections.
         */
        while (true) {
            Socket socket = serverSocket.accept();
            MenuHandler menuHandler = new MenuHandler(socket,serverGUI);
            menuHandler.start();
        }
    }

    /**
     * The entry point for the server application.
     * It creates a new `RestaurantServer` instance and calls its `work()` method
     * to start listening for client connections. After the `work()` method
     * (which is an infinite loop in this case, implying external termination),
     * it attempts to close the server socket.
     *
     * @param args Command line arguments (not used in this application).
     * @throws IOException If an I/O error occurs during server operation or setup.
     */
    public static void main(String[] args) throws IOException {
        RestaurantServer server = new RestaurantServer();
        server.work();
        /**
         * Closes the server socket when the `work()` method exits.
         * In this design, `work()` runs indefinitely, so this line would typically
         * only be reached upon abnormal termination or if the loop condition were to change.
         */
        server.serverSocket.close();
    }

}