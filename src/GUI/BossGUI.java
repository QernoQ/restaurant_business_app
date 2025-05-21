package GUI;

public class BossGUI extends BaseGUI {
    public BossGUI(String title) {
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
