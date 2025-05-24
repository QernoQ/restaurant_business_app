package server;

public class WindowManager {
    private static boolean addWorkerOpen = false;
    private static boolean manageWorkerOpen = false;

    public static synchronized boolean tryOpenAddWorker() {
        if (!addWorkerOpen) {
            addWorkerOpen = true;
            return true;
        }
        return false;
    }
    public static synchronized void closeAddWorker() {
        addWorkerOpen = false;
    }

    public static synchronized boolean tryOpenManageWorker() {
        if (!manageWorkerOpen) {
            manageWorkerOpen = true;
            return true;
        }
        return false;
    }

    public static synchronized void closeManageWorker() {
        manageWorkerOpen = false;
    }
}
