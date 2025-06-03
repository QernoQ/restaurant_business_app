package GUI.waiter;

import GUI.WaiterGUI;
import model.Bill;
import model.Food;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * The `ManageBillWindow` class provides a JDialog for waiters to manage an existing bill.
 * This includes viewing ordered items, removing items, adding new items, and closing the bill.
 */
public class ManageBillWindow extends JDialog implements ActionListener {
    /**
     * Output stream to send objects to the server.
     */
    private ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects from the server.
     */
    private ObjectInputStream objectIn;
    /**
     * The list of `Food` objects currently on the bill.
     */
    private List<Food> foodList;
    /**
     * The main panel of the dialog.
     */
    private JPanel mainPanel;
    /**
     * Panel to display the list of food items on the bill.
     */
    private JPanel foodListPanel;
    /**
     * Panel to hold action buttons.
     */
    private JPanel buttonPanel;
    /**
     * Reference to the main `WaiterGUI` window.
     */
    private WaiterGUI waiterGUI;
    /**
     * Button to go back without finalizing changes.
     */
    private JButton backButton;
    /**
     * Button to close the bill.
     */
    private JButton closeButton;
    /**
     * Button to add new items to the bill.
     */
    private JButton addItemButtton;
    /**
     * The ID of the bill being managed.
     */
    private int billID;
    /**
     * The button from the `CurrentBillWindow` that opened this dialog, used for removal.
     */
    private JButton billButton;

    /**
     * An instance of `AddBillWindow` used for selecting and adding new food items.
     */
    private AddBillWindow addBillWindow;

    /**
     * Constructs a new `ManageBillWindow`.
     *
     * @param parent        The parent `JDialog` (expected to be `CurrentBillWindow`).
     * @param waiterGUI     The main `WaiterGUI` instance.
     * @param objectOut     The `ObjectOutputStream` for communication with the server.
     * @param objectIn      The `ObjectInputStream` for communication with the server.
     * @param foodList      The list of food items currently on the bill.
     * @param billID        The ID of the bill to manage.
     * @param button        The `JButton` from the `CurrentBillWindow` that triggered this dialog.
     * @param addBillWindow An instance of `AddBillWindow` for adding items to the bill.
     */
    public ManageBillWindow(JDialog parent, WaiterGUI waiterGUI, ObjectOutputStream objectOut, ObjectInputStream objectIn, List<Food> foodList, int billID, JButton button, AddBillWindow addBillWindow) {
        super(parent, "Manage Bill", ModalityType.APPLICATION_MODAL);
        this.waiterGUI = waiterGUI;
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.foodList = foodList;
        this.billID = billID;
        this.billButton = button;
        this.addBillWindow = addBillWindow;
        init(parent);
    }

    /**
     * Initializes the components and layout of the `ManageBillWindow`.
     *
     * @param parent The parent `JDialog` to which this dialog is relative.
     */
    private void init(JDialog parent) {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(60, 60, 60));

        foodListPanel = new JPanel();
        foodListPanel.setLayout(new BoxLayout(foodListPanel, BoxLayout.Y_AXIS));
        foodListPanel.setBackground(new Color(60, 60, 60));
        JScrollPane scrollPane = new JScrollPane(foodListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ordered Items"));

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(60, 60, 60));

        closeButton = new JButton("Close Bill");
        backButton = new JButton("Back");
        addItemButtton = new JButton("Add Item");

        waiterGUI.styleButton(closeButton);
        waiterGUI.styleButton(backButton);
        waiterGUI.styleButton(addItemButtton);


        closeButton.addActionListener(this);
        backButton.addActionListener(this);
        addItemButtton.addActionListener(this);

        buttonPanel.add(backButton);
        buttonPanel.add(closeButton);
        buttonPanel.add(addItemButtton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        for (Food food : foodList) {
            addItem(food);
        }

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Adds a single food item to the display panel, along with a "Remove" button.
     *
     * @param food The `Food` object to add to the display.
     */
    private void addItem(Food food) {
        JPanel foodPanel = new JPanel(new BorderLayout());
        foodPanel.setBackground(new Color(70, 70, 70));
        foodPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel foodLabel = new JLabel(food.getName());
        waiterGUI.style(foodLabel, waiterGUI.labelFont, waiterGUI.labelColor, waiterGUI.labelBg);

        JButton removeButton = new JButton("Remove");
        waiterGUI.styleButton(removeButton);
        removeButton.addActionListener(e -> {
            foodList.remove(food);
            foodListPanel.remove(foodPanel);
            foodListPanel.revalidate();
            foodListPanel.repaint();
            pack();
        });

        foodPanel.add(foodLabel, BorderLayout.CENTER);
        foodPanel.add(removeButton, BorderLayout.EAST);

        foodListPanel.add(foodPanel);
    }

    /**
     * Opens the `AddBillWindow` to allow the waiter to select new items.
     * Adds the selected items to the current `foodList` and updates the display.
     * Sends an update to the server to save the added items.
     */
    public void addMenuItem() {
        addBillWindow.setVisible(true);
        List<Food> tempList = addBillWindow.getCurrentOrder();
        for (Food f : tempList) {
            foodList.add(f);
            addItem(f);
        }
        try {
            objectOut.writeObject(SortEnum.ADDITEMSAVE);
            objectOut.flush();
            objectOut.writeObject(new Bill(true, true, foodList, billID));
            objectOut.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Something went wrong while adding a bill.");
        }
        foodListPanel.revalidate();
        foodListPanel.repaint();
        pack();
    }

    /**
     * Handles action events generated by buttons in the dialog.
     *
     * @param e The `ActionEvent` object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == closeButton) {
                // Send CLOSEBILL command and bill details to server
                objectOut.writeObject(SortEnum.CLOSEBILL);
                objectOut.flush();
                objectOut.writeObject(new Bill(false, false, foodList, billID)); // Set bill as inactive and not done
                objectOut.flush();
                // Remove the corresponding button from the parent window
                if (billButton != null) {
                    Container parent = billButton.getParent();
                    if (parent != null) {
                        parent.remove(billButton);
                        parent.revalidate();
                        parent.repaint();
                    }
                }
                check();
            } else if (source == backButton) {
                objectOut.writeObject(SortEnum.BACKBILL);
                objectOut.flush();
                objectOut.writeObject(new Bill(true, true, foodList, billID));
                objectOut.flush();
                check();
            } else if (source == addItemButtton) {
                addMenuItem();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Reads a response from the server after an action (close/back).
     * Attempts to unlock the bill on the server and then disposes of the dialog.
     */
    public void check() {
        try {
            Object response = objectIn.readObject();
            try {
                objectOut.writeInt(billID);
                objectOut.flush();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to unlock bill: " + ex.getMessage());
            }
            if (response instanceof SortEnum) {
                dispose();
            }
        } catch (ClassNotFoundException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading Bill", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}