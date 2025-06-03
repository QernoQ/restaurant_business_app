package server;

import java.io.*;
import java.net.Socket;

/**
 * The `MenuHandler` class is responsible for handling the initial login and menu selection
 * for a client connecting to the server. It authenticates the client's requested role
 * and, upon successful validation, delegates further communication to a `ClientHandler`.
 * This handler runs in its own thread to manage concurrent client connections.
 */
/**
 * @author Patryk Bocheński
 */
public class MenuHandler extends BaseHandler {
    /**
     * Output stream to send objects (like login responses) to the client.
     */
    protected ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects (like login attempts) from the client.
     */
    protected ObjectInputStream objectIn;

    /**
     * Constructs a new `MenuHandler`.
     *
     * @param socket    The `Socket` connected to the client.
     * @param serverGUI A reference to the `ServerGUI` for displaying messages and logging server activities.
     */
    public MenuHandler(Socket socket, ServerGUI serverGUI) {
        super(socket, serverGUI);
    }

    /**
     * The main execution method for the `MenuHandler` thread.
     * It initializes the object input/output streams and enters a loop to read client login attempts.
     * It validates the login against predefined roles (`boss`, `manager`, `waiter`, `cook`).
     * Upon a valid login, it sends the role back to the client, logs the selection, and
     * then spawns a new `ClientHandler` to manage the ongoing communication for that client.
     * For invalid logins, it sends an "invalidLogin" response to the client and logs the attempt.
     * Handles I/O and class-not-found exceptions that may occur during communication.
     */
    public void run() {
        try {
            this.objectOut = new ObjectOutputStream(socket.getOutputStream());
            this.objectIn = new ObjectInputStream(socket.getInputStream());

            String chooseMenu;

            /**
             * Continuously reads login attempts from the client until the client disconnects or a valid menu is chosen.
             */
            while ((chooseMenu = (String) objectIn.readObject()) != null) {
                chooseMenu = chooseMenu.toLowerCase();
                switch (chooseMenu) {
                    case "boss":
                    case "manager":
                    case "waiter":
                    case "cook":
                        objectOut.writeObject(chooseMenu);
                        serverGUI.displayMessage(chooseMenu.toUpperCase() + " Menu has been selected by : " + socket.getInetAddress().getHostAddress());
                        /**
                         * Upon successful login, a new `ClientHandler` is started to take over communication for this client.
                         * The `MenuHandler` then completes its execution for this client.
                         */
                        new ClientHandler(socket, serverGUI).start();
                        return;
                    default:
                        objectOut.writeObject("invalidLogin");
                        serverGUI.displayMessage("Invalid login!");
                        break;
                }
            }

        } catch (IOException e) {
            serverGUI.displayMessage("[MENU HANDLER] " + e.getMessage());
        } catch (ClassNotFoundException e) {
            serverGUI.displayMessage("[MENU HANDLER] " + e.getMessage());
        }
    }
}