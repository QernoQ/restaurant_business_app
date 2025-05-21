package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public abstract class BaseGUI extends JFrame {

    public BaseGUI(String title) {
        super(title);
    }
    protected abstract void init();
}
