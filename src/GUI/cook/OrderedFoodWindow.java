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

/**
 * The `OrderedFoodWindow` class represents a JDialog window for cooks to view and manage
 * incoming food orders. It displays active food orders and allows the cook to interact with them.
 */
public class OrderedFoodWindow extends JDialog {

    /**
     * Output stream to send objects to the server.
     */
    private ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects from the server.
     */
    private ObjectInputStream objectIn;

    /**
     * A list of `Bill` objects received from the server, representing current orders.
     */
    private List<Bill> billRecived;

    /**
     * A list of `Food` objects within a selected bill, representing the food items in that order.
     */
    private List<Food> foodList;

    /**
     * A flag indicating whether the window has already been closed.
     */
    private boolean alreadyClosed = false;
    /**
     * The main panel where order buttons are displayed.
     */
    private JPanel mainPanel;

    /**
     * The ID of the food item or bill currently being processed.
     */
    private int foodID;
    /**
     * Reference to the main `CookGUI` window.
     */
    private CookGUI CookGUI;

    /**
     * A timer used to periodically refresh the displayed orders.
     */
    private Timer refreshTimer;


    /**
     * Constructs a new `OrderedFoodWindow`.
     *
     * @param parent    The parent `JFrame` of this dialog, expected to be a `CookGUI` instance.
     * @param objectOut The `ObjectOutputStream` for communication with the server.
     * @param objectIn  The `ObjectInputStream` for communication with the server.
     */
    public OrderedFoodWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Check ordered food", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        CookGUI = (CookGUI) parent;
        init(parent);
    }

    /**
     * Initializes the components and layout of the `OrderedFoodWindow`.
     *
     * @param parent The parent `JFrame` to which this dialog is relative.
     */
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
            /**
             * Invoked when the window is first opened.
             * Initiates the first refresh of orders and starts the refresh timer.
             * @param e The window event.
             */
            public void windowOpened(WindowEvent e) {
                refreshOrders();
                refreshTimer = new Timer(5000, evt -> refreshOrders());
                refreshTimer.start();
            }

            /**
             * Invoked when the user attempts to close the window.
             * Stops the refresh timer to prevent further updates.
             * @param e The window event.
             */
            public void windowClosing(WindowEvent e) {
                if (refreshTimer != null) {
                    refreshTimer.stop();
                }
            }
        });
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    /**
     * Adds a button to the `mainPanel` for a given `Bill`.
     * Each button represents an active order and, when clicked, opens a `SeeOrderedFood` dialog.
     *
     * @param bill The `Bill` object for which to create a button.
     */
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

    /**
     * Refreshes the displayed list of orders.
     * Clears the current display, requests updated bill information from the server,
     * filters for food orders (excluding drinks), and adds buttons for active orders.
     * If no food orders are active, the window is closed.
     */
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