package GUI.cook;

import GUI.waiter.AddBillWindow;
import GUI.waiter.CurrentBillWindow;
import GUI.waiter.ManageBillWindow;
import model.Bill;
import model.Food;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class OrderedFoodWindow extends JDialog {

    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;

    private List<Bill> billRecived;

    private List<Food> foodList;

    private JPanel mainPanel;

    private int foodID;


    public OrderedFoodWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Check ordered food", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        init(parent);
    }

    void init(JFrame parent) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        mainPanel = new JPanel(new GridLayout(2, 2));
        mainPanel.setBackground(new Color(60, 60, 60));

        JScrollPane menuScroll = new JScrollPane(mainPanel);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Ordered Food"));
        setLocationRelativeTo(parent);

        addWindowListener(new WindowAdapter() {
            boolean active = false;

            public void windowOpened(WindowEvent e) {
                try {
                    objectOut.writeObject(SortEnum.READBILL);
                    objectOut.flush();
                    billRecived = (List<Bill>) objectIn.readObject();
                    for (Bill bill : billRecived) {
                        if (bill.isActive()) {
                            addButton(bill);
                            active = true;
                        }
                    }
                    if (!active) {
                        dispose();
                        JOptionPane.showMessageDialog(OrderedFoodWindow.this, "No ordered Food!");
                    }
                    revalidate();
                    repaint();
                    pack();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(OrderedFoodWindow.this, ex.getMessage());
                }
            }
        });
        setVisible(true);

    }

    public void addButton(Bill bill) {
        JButton button = new JButton("Order " + bill.getBillId());
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 100));
        mainPanel.add(button);
        foodList = bill.getCurrentOrder();
        foodID = bill.getBillId();

    }
}
