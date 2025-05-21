package server;

import com.sun.source.tree.WhileLoopTree;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RestaurantServer {
    public static final int serverPort = 8888;
    private ServerSocket serverSocket;

    public RestaurantServer() throws IOException {
        try {
            serverSocket = new ServerSocket(serverPort);
            new ServerGUI("Server");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Socket could not be created!");
            System.exit(0);
        }
    }

    void work() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            DataBase dataBase = new DataBase(socket);
            dataBase.start();
        }
    }

    public static void main(String[] args) throws IOException {
        RestaurantServer server = new RestaurantServer();
        server.work();
        server.serverSocket.close();
    }

}




