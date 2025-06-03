package GUI.cook;

import GUI.CookGUI;
import model.Bill;
import model.Food;
import model.FoodCategoryEnum;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class OrderedFoodWindow extends JDialog {

    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;

    private List<Bill> billRecived;

    private List<Food> foodList;

    private boolean alreadyClosed = false;
    private JPanel mainPanel;

    private int foodID;
    private CookGUI CookGUI;

    private Timer refreshTimer;


    public OrderedFoodWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Check ordered food", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        CookGUI = (CookGUI) parent;
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
            public void windowOpened(WindowEvent e) {
                refreshOrders();
                refreshTimer = new Timer(5000, evt -> refreshOrders());
                refreshTimer.start();
            }

            public void windowClosing(WindowEvent e) {
                if (refreshTimer != null) {
                    refreshTimer.stop();
                }
            }
        });
        add(mainPanel, BorderLayout.CENTER);
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
        button.addActionListener(e -> {
            foodList = bill.getCurrentOrder();
            foodID = bill.getBillId();
            new SeeOrderedFood(this, CookGUI, objectOut, objectIn, foodList, foodID, button);
        });
    }

    private void refreshOrders() {
        if (alreadyClosed) return;
        mainPanel.removeAll();
        boolean active = false;

        try {
            objectOut.writeObject(SortEnum.READBILL);
            objectOut.flush();
            billRecived = (List<Bill>) objectIn.readObject();

            for (Bill bill : billRecived) {
                if (bill.isDone()) {
                    List<Food> filteredFood = bill.getCurrentOrder().stream()
                            .filter(f -> !(f.getCategory().equals(FoodCategoryEnum.HOT_DRINKS) ||
                                    f.getCategory().equals(FoodCategoryEnum.COLD_DRINKS) ||
                                    f.getCategory().equals(FoodCategoryEnum.BEER)))
                            .toList();

                    if (!filteredFood.isEmpty()) {
                        addButton(bill);
                        active = true;
                    }
                }
            }

            if (!active) {
                alreadyClosed = true;
                if (refreshTimer != null) refreshTimer.stop();
                dispose();
                JOptionPane.showMessageDialog(OrderedFoodWindow.this, "No ordered Food!");
            }

            revalidate();
            repaint();
            pack();

        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error updating orders: " + ex.getMessage());
        }
    }
}
