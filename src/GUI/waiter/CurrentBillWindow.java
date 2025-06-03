package GUI.waiter;

import GUI.WaiterGUI;
import GUI.cook.OrderedFoodWindow;
import model.Bill;
import model.Food;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

/**
 * The `CurrentBillWindow` class represents a JDialog window that displays all currently active bills.
 * It allows waiters to view and manage these bills, including editing them.
 */
public class CurrentBillWindow extends JDialog {
    /**
     * A timer that periodically refreshes the displayed bills.
     */
    private Timer refreshTimer;
    /**
     * Output stream to send objects to the server.
     */
    private ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects from the server.
     */
    private ObjectInputStream objectIn;
    /**
     * Reference to the main `WaiterGUI` window.
     */
    private WaiterGUI waiterGUI;
    /**
     * The main panel where bill buttons are displayed.
     */
    private JPanel mainPanel;
    /**
     * A list of `Food` objects belonging to a selected bill.
     */
    private List<Food> foodList;
    /**
     * A list of `Bill` objects received from the server, representing current bills.
     */
    private List<Bill> billRecived;
    /**
     * The ID of the food item or bill currently being processed.
     */
    private int foodID;
    /**
     * An instance of `AddBillWindow` used for managing bill details.
     */
    private AddBillWindow addBillWindow;

    /**
     * Constructs a new `CurrentBillWindow`.
     *
     * @param parent    The parent `JFrame` of this dialog, expected to be a `WaiterGUI` instance.
     * @param objectOut The `ObjectOutputStream` for communication with the server.
     * @param objectIn  The `ObjectInputStream` for communication with the server.
     */
    public CurrentBillWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Add Bill", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.waiterGUI = (WaiterGUI) parent;
        init(parent);
    }

    /**
     * Initializes the components and layout of the `CurrentBillWindow`.
     *
     * @param parent The parent `Window` to which this dialog is relative.
     */
    public void init(Window parent) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        mainPanel = new JPanel(new GridLayout(2, 2));
        mainPanel.setBackground(new Color(60, 60, 60));

        JScrollPane menuScroll = new JScrollPane(mainPanel);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        setLocationRelativeTo(parent);
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when the window is first opened.
             * Initiates the first refresh of orders and starts the refresh timer.
             * @param e The window event.
             */
            public void windowOpened(WindowEvent e) {
                refreshOrders(); // Initial refresh
                refreshTimer = new Timer(5000, evt -> refreshOrders()); // Refresh every 5 seconds
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
     * Each button represents an active bill and, when clicked, attempts to open a `ManageBillWindow`.
     * It includes logic for locking the bill to prevent simultaneous edits.
     *
     * @param bill The `Bill` object for which to create a button.
     */
    public void addButton(Bill bill) {
        JButton button = new JButton("Bill " + bill.getBillId());
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setBackground(new Color(50, 50, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 100));
        mainPanel.add(button);
        button.addActionListener(e -> {
            try {
                objectOut.writeObject("TRY_LOCK_BILL");
                objectOut.flush();
                objectOut.writeInt(bill.getBillId());
                objectOut.flush();
                SortEnum response = (SortEnum) objectIn.readObject();

                if(response == SortEnum.BILL_LOCK_SUCCESS) {
                    foodList = bill.getCurrentOrder();
                    foodID = bill.getBillId();
                    new ManageBillWindow(this,waiterGUI,objectOut, objectIn, foodList, foodID,button,addBillWindow = new AddBillWindow(waiterGUI, objectOut, objectIn,false,true));
                } else if(response == SortEnum.BILL_ALREADY_LOCKED) {
                    JOptionPane.showMessageDialog(this, "This bill is currently being edited by another waiter.");
                }
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error while trying to open the bill: " + ex.getMessage());
            }


        });
    }

    /**
     * Refreshes the displayed list of active bills.
     * Clears the current display, requests updated bill information from the server,
     * and adds buttons for all active bills. If no active bills are found, the window is closed.
     */
    private void refreshOrders() {
        mainPanel.removeAll();
        boolean active = false;

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
                JOptionPane.showMessageDialog(CurrentBillWindow.this, "No active bills!");
            }

            mainPanel.revalidate();
            mainPanel.repaint();
            pack();

        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error updating bills: " + ex.getMessage());
        }
    }
}