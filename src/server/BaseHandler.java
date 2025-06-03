package server;

import java.net.Socket;

/**
 * The `BaseHandler` class serves as an abstract base for all client-specific handlers
 * within the server application. Each instance of a subclass of `BaseHandler`
 * is intended to manage communication with a single connected client.
 * It extends `Thread` to allow concurrent handling of multiple clients.
 */
public class BaseHandler extends Thread {
    /**
     * The `Socket` connected to the client that this handler will manage.
     */
    protected final Socket socket;
    /**
     * A reference to the `ServerGUI` instance, allowing handlers to interact with
     * and update the server's graphical user interface (e.g., logging events).
     */
    protected final ServerGUI serverGUI;

    /**
     * Constructs a new `BaseHandler`.
     *
     * @param socket    The `Socket` object representing the connection to a client.
     * @param serverGUI A reference to the `ServerGUI` instance where server events can be logged or displayed.
     */
    public BaseHandler(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGUI = serverGUI;
    }
}