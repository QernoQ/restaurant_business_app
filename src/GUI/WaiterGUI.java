package GUI;

import java.net.Socket;

public class WaiterGUI extends BaseGUI {
    public WaiterGUI(String title, Socket socket) {
        super(title,socket);
        init();
    }

    @Override
    protected void init() {
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

    @Override
    protected void close() {

    }
}
