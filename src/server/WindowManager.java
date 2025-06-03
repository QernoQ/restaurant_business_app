package server;

/**
 * The `WindowManager` class is a utility class designed to manage the state
 * of specific GUI windows on the client side, particularly for boss-related
 * functionalities like "Add Worker" and "Manage Workers." It ensures that
 * only one instance of these critical windows can be open at a time for
 * any connected client, preventing potential conflicts or multiple simultaneous edits.
 * This is achieved through static synchronized methods, making it thread-safe.
 */
/**
 * @author Patryk Bocheński
 */
public class WindowManager {
    /**
     * A static boolean flag indicating whether the "Add Worker" window is currently open.
     * Initialized to `false`, meaning the window is closed by default.
     */
    private static boolean addWorkerOpen = false;
    /**
     * A static boolean flag indicating whether the "Manage Worker" window is currently open.
     * Initialized to `false`, meaning the window is closed by default.
     */
    private static boolean manageWorkerOpen = false;

    /**
     * Attempts to open the "Add Worker" window.
     * This method is `synchronized` to ensure thread safety, meaning only one thread
     * can check and modify `addWorkerOpen` at a time.
     *
     * @return `true` if the window was successfully opened (i.e., it was not already open),
     * `false` otherwise (if the window is already open).
     */
    public static synchronized boolean tryOpenAddWorker() {
        if (!addWorkerOpen) {
            addWorkerOpen = true;
            return true;
        }
        return false;
    }

    /**
     * Closes the "Add Worker" window.
     * This method is `synchronized` to ensure thread safety when updating the `addWorkerOpen` flag.
     * It sets the flag to `false`, indicating that the window is now closed.
     */
    public static synchronized void closeAddWorker() {
        addWorkerOpen = false;
    }

    /**
     * Attempts to open the "Manage Worker" window.
     * This method is `synchronized` to ensure thread safety, meaning only one thread
     * can check and modify `manageWorkerOpen` at a time.
     *
     * @return `true` if the window was successfully opened (i.e., it was not already open),
     * `false` otherwise (if the window is already open).
     */

    public static synchronized boolean tryOpenManageWorker() {
        if (!manageWorkerOpen) {
            manageWorkerOpen = true;
            return true;
        }
        return false;
    }

    /**
     * Closes the "Manage Worker" window.
     * This method is `synchronized` to ensure thread safety when updating the `manageWorkerOpen` flag.
     * It sets the flag to `false`, indicating that the window is now closed.
     */
    public static synchronized void closeManageWorker() {
        manageWorkerOpen = false;
    }
}