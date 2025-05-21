package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class BaseGUI extends JFrame {
    public BaseGUI(String title) {
        super(title);
        init();
    }
    protected abstract void init();
}
