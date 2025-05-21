package GUI;

public class CookGUI extends BaseGUI {
    public CookGUI(String title) {
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
