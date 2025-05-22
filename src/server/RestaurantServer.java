package server;

import com.sun.source.tree.WhileLoopTree;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RestaurantServer {
    public static final int serverPort = 8888;
    private ServerSocket serverSocket;
    private ServerGUI serverGUI;

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

    void work() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            MenuHandler menuHandler = new MenuHandler(socket,serverGUI);
            menuHandler.start();
        }
    }

    public static void main(String[] args) throws IOException {
        RestaurantServer server = new RestaurantServer();
        server.work();
        server.serverSocket.close();
    }

}




