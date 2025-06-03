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
 * The `SeeOrderedFood` class represents a JDialog window for cooks to view the details
 * of a specific food order and mark it as completed.
 */
public class SeeOrderedFood extends JDialog implements ActionListener {

    /**
     * Output stream to send objects to the server.
     */
    private ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects from the server.
     */
    private ObjectInputStream objectIn;
    /**
     * The complete list of food items in the current order.
     */
    private List<Food> foodList;
    /**
     * A filtered list of food items, excluding drinks, for display.
     */
    private List<Food> filteredFoodList;
    /**
     * The main panel of the dialog.
     */
    private JPanel mainPanel;
    /**
     * Panel to display the list of food items.
     */
    private JPanel foodListPanel;
    /**
     * Panel to hold action buttons.
     */
    private JPanel buttonPanel;
    /**
     * Reference to the main `CookGUI` window.
     */
    private CookGUI cookGUI;
    /**
     * Button to close the dialog.
     */
    private JButton closeButton;
    /**
     * Button to remove (mark as complete) the current order.
     */
    private JButton removeOrderButton;
    /**
     * Button to refresh the order details.
     */
    private JButton refreshButton;
    /**
     * The ID of the bill associated with this order.
     */
    private int billID;
    /**
     * The button from the `OrderedFoodWindow` that opened this dialog, used for removal.
     */
    private JButton orderButton;

    /**
     * Constructs a new `SeeOrderedFood` dialog.
     *
     * @param parent     The parent `JDialog` (expected to be `OrderedFoodWindow`).
     * @param cookGUI    The main `CookGUI` instance.
     * @param objectOut  The `ObjectOutputStream` for communication with the server.
     * @param objectIn   The `ObjectInputStream` for communication with the server.
     * @param foodList   The list of food items in the order to display.
     * @param billID     The ID of the bill associated with this order.
     * @param button     The `JButton` from the `OrderedFoodWindow` that triggered this dialog.
     */
    public SeeOrderedFood(JDialog parent, CookGUI cookGUI, ObjectOutputStream objectOut, ObjectInputStream objectIn, List<Food> foodList, int billID, JButton button) {
        super(parent, "See Order", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.foodList = foodList;
        this.billID = billID;
        this.orderButton = button;
        this.cookGUI = cookGUI;
        init(parent);
    }

    /**
     * Initializes the components and layout of the `SeeOrderedFood` dialog.
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

        closeButton = new JButton("Back");
        removeOrderButton = new JButton("Remove Order");
        refreshButton = new JButton("Refresh");

        cookGUI.styleButton(closeButton);
        cookGUI.styleButton(removeOrderButton);
        cookGUI.styleButton(refreshButton);

        closeButton.addActionListener(this);
        removeOrderButton.addActionListener(this);
        refreshButton.addActionListener(this);

        buttonPanel.add(closeButton);
        buttonPanel.add(removeOrderButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        filteredFoodList = foodList.stream()
                .filter(f -> !(f.getCategory().equals(FoodCategoryEnum.HOT_DRINKS) ||
                        f.getCategory().equals(FoodCategoryEnum.COLD_DRINKS) ||
                        f.getCategory().equals(FoodCategoryEnum.BEER)))
                .toList();

        for (Food food : filteredFoodList) {
            addItem(food);
        }


        pack();
        setLocationRelativeTo(parent);

        setVisible(true);
    }

    /**
     * Adds a single food item to the display panel.
     *
     * @param food The `Food` object to add.
     */
    private void addItem(Food food) {
        JPanel foodPanel = new JPanel(new BorderLayout());
        foodPanel.setBackground(new Color(70, 70, 70));
        foodPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel foodLabel = new JLabel(food.getName());
        cookGUI.style(foodLabel, cookGUI.labelFont, cookGUI.labelColor, cookGUI.labelBg);
        foodPanel.add(foodLabel, BorderLayout.CENTER);
        foodListPanel.add(foodPanel);
        foodListPanel.revalidate();
        foodListPanel.repaint();
        pack();
    }

    /**
     * Refreshes the displayed order by re-fetching the bill details from the server.
     */
    private void refreshOrder() {
        try {
            objectOut.writeObject(SortEnum.READBILL);
            objectOut.flush();

            List<Bill> bills = (List<Bill>) objectIn.readObject();
            for (Bill bill : bills) {
                if (bill.getBillId() == billID) {
                    this.foodList = bill.getCurrentOrder();
                    filteredFoodList = foodList.stream()
                            .filter(f -> !(f.getCategory().equals(FoodCategoryEnum.HOT_DRINKS) ||
                                    f.getCategory().equals(FoodCategoryEnum.COLD_DRINKS) ||
                                    f.getCategory().equals(FoodCategoryEnum.BEER)))
                            .toList();

                    foodListPanel.removeAll();
                    for (Food food : filteredFoodList) {
                        addItem(food);
                    }
                    foodListPanel.revalidate();
                    foodListPanel.repaint();
                    pack();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erorr while refreshing " + ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles action events generated by buttons in the dialog.
     *
     * @param e The `ActionEvent` object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == closeButton) {
            dispose();
        } else if (source == removeOrderButton) {
            dispose();
            if (orderButton != null) {
                Container parent = orderButton.getParent();
                if (parent != null) {
                    parent.remove(orderButton);
                    parent.revalidate();
                    parent.repaint();
                    try {
                        objectOut.writeObject(SortEnum.REMOVEORDER);
                        objectOut.flush();
                        objectOut.writeObject(new Bill(true, false, foodList, billID));
                        objectOut.flush();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(cookGUI, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else if (source == refreshButton) {
            refreshOrder();
        }
    }
}