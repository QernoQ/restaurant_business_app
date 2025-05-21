package GUI;

import javax.swing.*;

public class CookGUI extends BaseGUI {
    public CookGUI(String title) {
        super(title);
        init();
    }

    @Override
    protected void init() {
        JFrame frame = new JFrame();
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

    }
}
