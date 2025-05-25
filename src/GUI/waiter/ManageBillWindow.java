package GUI.waiter;

import javax.swing.*;
import java.io.ObjectOutputStream;

public class ManageBillWindow extends JDialog {
    private ObjectOutputStream objectOut;
    public ManageBillWindow(JFrame parent, ObjectOutputStream objectOut) {
        super(parent, "Add Bill", true);
        this.objectOut = objectOut;
        init(parent);
    }
    public void init(JFrame parent) {

    }
}
