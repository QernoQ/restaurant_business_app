package server;

import java.net.Socket;

public class BaseHandler extends Thread {
    protected final Socket socket;
    protected final ServerGUI serverGUI;

    public BaseHandler(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGUI = serverGUI;
    }
}
