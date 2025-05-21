package GUI;

public class WaiterGUI extends BaseGUI {
    public WaiterGUI(String title) {
        super(title);
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
}
