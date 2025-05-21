package server;

import java.net.Socket;

public class DataBase extends Thread {

    private final Socket socket;

    public DataBase(Socket socket) {
        this.socket = socket;
    }
}
