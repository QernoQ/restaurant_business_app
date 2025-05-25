package GUI;

import javax.swing.*;
import java.net.Socket;

public class CookGUI extends BaseGUI {
    public CookGUI(String title, Socket socket) {
        super(title,socket);
        init();
    }
 //  👩🏻‍🍳
    @Override
    protected void init() {
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

}
