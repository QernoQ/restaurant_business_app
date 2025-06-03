package GUI.waiter;

import GUI.BaseGUI;
import GUI.BossGUI;
import GUI.WaiterGUI;
import model.Bill;
import model.Food;
import model.FoodCategoryEnum;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
/**
 * @author Patryk Bocheński
 */
/**
 * The `AddBillWindow` class represents a JDialog window where a waiter can create and manage a new bill.
 * It displays menu items, allows adding/removing items from the order, and finalizing the bill.
 */
public class AddBillWindow extends JDialog implements ActionListener {

    /**
     * Output stream to send objects to the server.
     */
    private ObjectOutputStream objectOut;
    /**
     * Input stream to receive objects from the server.
     */
    private ObjectInputStream objectIn;
    /**
     * The main panel containing menu categories and items.
     */
    private JPanel mainPanel;
    /**
     * Text area to display the currently ordered items.
     */
    private JTextArea orderDisplay;
    /**
     * A list of `Food` objects representing the items in the current order.
     */
    private List<Food> currentOrder;
    /**
     * The total price of the current order.
     */
    private double totalPrice;
    /**
     * Button to remove the last added item from the order.
     */
    JButton removeLastButton;
    /**
     * Button to finalize (finish) the order and send it to the server.
     */
    JButton finishButton;
    /**
     * Button to clear all items from the current order.
     */
    JButton clearButton;
    /**
     * Button to save the bill (used in specific management scenarios).
     */
    JButton saveButton;
    /**
     * Reference to the main `WaiterGUI` window.
     */
    private WaiterGUI waiterGUI;
    /**
     * A boolean flag controlling the initial visibility of the dialog.
     */
    private boolean visible;
    /**
     * A boolean flag indicating if the buttons should be configured for management mode.
     */
    private boolean buttonsManage;

    /**
     * Constructs a new `AddBillWindow`.
     *
     * @param parent        The parent `JFrame` of this dialog, expected to be a `WaiterGUI` instance.
     * @param objectOut     The `ObjectOutputStream` for communication with the server.
     * @param objectIn      The `ObjectInputStream` for communication with the server.
     * @param visible       A boolean to set the initial visibility of the window.
     * @param buttonsManage A boolean to configure button enablement for management mode.
     */
    public AddBillWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn,boolean visible,boolean buttonsManage) {
        super(parent, "Add Bill", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.waiterGUI = (WaiterGUI) parent;
        this.currentOrder = new ArrayList<>();
        this.totalPrice = 0.0;
        this.visible = visible;
        this.buttonsManage = buttonsManage;
        init(parent);
    }

    /**
     * Initializes the components and layout of the `AddBillWindow`.
     *
     * @param parent The parent `JFrame` to which this dialog is relative.
     */
    public void init(JFrame parent) {


        setTitle("Add Bill");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.darkGray);
        JScrollPane menuScroll = new JScrollPane(mainPanel);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu Items"));

        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBorder(BorderFactory.createTitledBorder("Current Order"));

        orderDisplay = new JTextArea(10, 30);
        orderDisplay.setEditable(false);
        orderDisplay.setBackground(Color.darkGray);
        orderDisplay.setForeground(Color.white);
        orderDisplay.setFont(new Font("Serif", Font.BOLD, 14));
        JScrollPane orderScroll = new JScrollPane(orderDisplay);
        orderPanel.add(orderScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        finishButton = new JButton("Finish Order");
        finishButton.addActionListener(this);
        buttonPanel.add(finishButton);

        removeLastButton = new JButton("Remove Last");
        removeLastButton.addActionListener(this);
        buttonPanel.add(removeLastButton);


        clearButton = new JButton("Clear All");
        clearButton.addActionListener(this);
        buttonPanel.add(clearButton);

        saveButton = new JButton("Save Bill");
        saveButton.addActionListener(this);
        buttonPanel.add(saveButton);

        orderPanel.add(buttonPanel, BorderLayout.SOUTH);

        Map<FoodCategoryEnum, List<Food>> menuItems = createMenuItems();
        for (Map.Entry<FoodCategoryEnum, List<Food>> entry : menuItems.entrySet()) {
            addCategory(entry.getKey(), entry.getValue());
        }

        add(menuScroll, BorderLayout.CENTER);
        add(orderPanel, BorderLayout.EAST);
        setSize(1200, 800);
        setLocationRelativeTo(parent);
        waiterGUI.styleButton(finishButton);
        waiterGUI.styleButton(removeLastButton);
        waiterGUI.styleButton(clearButton);
        waiterGUI.styleButton(saveButton);
        buttonPanel.setBackground(Color.darkGray);
        if (buttonsManage) {
            finishButton.setEnabled(false);
            removeLastButton.setEnabled(false);
            clearButton.setEnabled(false);
            saveButton.setEnabled(true);
        } else {
            finishButton.setEnabled(true);
            removeLastButton.setEnabled(true);
            clearButton.setEnabled(true);
            saveButton.setEnabled(false);
        }
        setVisible(visible);
    }

    /**
     * Handles action events generated by buttons in the window.
     *
     * @param e The `ActionEvent` object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == clearButton) {
            clearOrder();

        } else if (source == removeLastButton) {
            removeLastItem();
        } else if (source == finishButton) {
            finishOrder();
        } else if (source == saveButton) {
            dispose();
        }

    }

    /**
     * Adds a category of food items to the main menu panel.
     * Each category has a label and a panel containing buttons for each food item.
     *
     * @param category The `FoodCategoryEnum` for the category.
     * @param items    A `List` of `Food` objects belonging to this category.
     */
    private void addCategory(FoodCategoryEnum category, List<Food> items) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        categoryPanel.setBackground(Color.darkGray);
        JLabel label = new JLabel("📋 " + category.name().replace("_", " "), SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(new Color(14, 94, 36));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        categoryPanel.add(label, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        itemsPanel.setBackground(Color.darkGray);

        for (Food food : items) {
            JButton button = new JButton("<html><center>" + food.getName() + "<br><b>" + (int) food.getPrice() + " $</b></center></html>");
            button.setFont(new Font("Serif", Font.PLAIN, 14));
            button.setBackground(new Color(50, 50, 50));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.addActionListener(e -> addToOrder(food));
            itemsPanel.add(button);
        }

        categoryPanel.add(itemsPanel, BorderLayout.CENTER);
        mainPanel.add(categoryPanel);
    }

    /**
     * Adds a selected food item to the current order and updates the total price and display.
     *
     * @param food The `Food` item to add to the order.
     */
    private void addToOrder(Food food) {
        currentOrder.add(food);
        totalPrice += food.getPrice();
        orderDisplay.append(food + "\n");
    }

    /**
     * Removes the last added item from the current order and updates the total price and display.
     */
    private void removeLastItem() {
        if (!currentOrder.isEmpty()) {
            Food removed = currentOrder.removeLast();
            totalPrice -= removed.getPrice();
            refreshOrderDisplay();
        }
    }

    /**
     * Clears all items from the current order and resets the total price and display.
     */
    private void clearOrder() {
        currentOrder.clear();
        totalPrice = 0.0;
        refreshOrderDisplay();
    }

    /**
     * Refreshes the display area to reflect the current items in the order.
     */
    private void refreshOrderDisplay() {
        orderDisplay.setText("");
        for (Food f : currentOrder) {
            orderDisplay.append(f.toString() + "\n");
        }
    }

    /**
     * Returns the list of `Food` items currently in the order.
     *
     * @return A `List` of `Food` objects representing the current order.
     */
    public List<Food> getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Finalizes the current order.
     * If the order is not empty, it sends the bill to the server and displays the total price.
     * Then, it closes the dialog.
     */
    private void finishOrder() {
        try {
            if(!currentOrder.isEmpty()) {
                objectOut.writeObject(SortEnum.ADDBILL);
                int id = (int) objectIn.readObject();
                objectOut.writeObject(new Bill(true,true, currentOrder, id));
                JOptionPane.showMessageDialog(this, "Order finished! Total price: " + totalPrice + " $!");
            } else {
                JOptionPane.showMessageDialog(this, "Order is empty!");
                dispose();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        dispose();
    }

    /**
     * Creates and returns a `Map` of menu items organized by `FoodCategoryEnum`.
     * This method defines the available food and drink items with their prices and categories.
     *
     * @return A `Map` where keys are `FoodCategoryEnum` and values are `List`s of `Food` objects.
     */
    private Map<FoodCategoryEnum, List<Food>> createMenuItems() {
        Map<FoodCategoryEnum, List<Food>> map = new LinkedHashMap<>();

        map.put(FoodCategoryEnum.DESSERTS, Arrays.asList(
                new Food("Ice Cream (3 scoops)", 10, FoodCategoryEnum.DESSERTS),
                new Food("Strawberry Shake", 10, FoodCategoryEnum.DESSERTS)));

        map.put(FoodCategoryEnum.COLD_DRINKS, Arrays.asList(
                new Food("Coca-Cola", 5, FoodCategoryEnum.COLD_DRINKS),
                new Food("Pepsi", 5, FoodCategoryEnum.COLD_DRINKS),
                new Food("Juice (glass)", 5, FoodCategoryEnum.COLD_DRINKS),
                new Food("Juice 1L", 10, FoodCategoryEnum.COLD_DRINKS),
                new Food("Juice 2L", 20, FoodCategoryEnum.COLD_DRINKS),
                new Food("Sprite", 5, FoodCategoryEnum.COLD_DRINKS),
                new Food("Fanta", 5, FoodCategoryEnum.COLD_DRINKS),
                new Food("Water 0.5L", 10, FoodCategoryEnum.COLD_DRINKS)));

        map.put(FoodCategoryEnum.HOT_DRINKS, Arrays.asList(
                new Food("Coffee", 5, FoodCategoryEnum.HOT_DRINKS),
                new Food("Tea", 5, FoodCategoryEnum.HOT_DRINKS)));

        map.put(FoodCategoryEnum.BEER, Arrays.asList(
                new Food("Tyskie", 7, FoodCategoryEnum.BEER),
                new Food("Książęce Wheat", 8, FoodCategoryEnum.BEER),
                new Food("Lech Premium", 7, FoodCategoryEnum.BEER),
                new Food("Redds", 8, FoodCategoryEnum.BEER)));

        map.put(FoodCategoryEnum.SOUPS, Arrays.asList(
                new Food("Goulash Soup", 10, FoodCategoryEnum.SOUPS),
                new Food("Sour Soup", 10, FoodCategoryEnum.SOUPS),
                new Food("Beef Tripe Soup", 10, FoodCategoryEnum.SOUPS),
                new Food("Soup of the Day", 10, FoodCategoryEnum.SOUPS)));

        map.put(FoodCategoryEnum.MAIN_COURSES, Arrays.asList(
                new Food("Pork Schnitzel", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Chicken Fillet", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Chicken w/ Vegetables", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Pork Shoulder", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Cod Fish", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Nuggets", 16, FoodCategoryEnum.MAIN_COURSES),
                new Food("Old Polish Platter", 25, FoodCategoryEnum.MAIN_COURSES),
                new Food("Large Polish Platter", 65, FoodCategoryEnum.MAIN_COURSES)));

        map.put(FoodCategoryEnum.PASTA_DISHES, Arrays.asList(
                new Food("Spaghetti Bolognese", 20, FoodCategoryEnum.PASTA_DISHES),
                new Food("Spaghetti w/ Spinach", 15, FoodCategoryEnum.PASTA_DISHES),
                new Food("Spaghetti Spinach+Chicken", 20, FoodCategoryEnum.PASTA_DISHES),
                new Food("Lasagne", 40, FoodCategoryEnum.PASTA_DISHES)));

        map.put(FoodCategoryEnum.DUMPLINGS_PIEROGI, Arrays.asList(
                new Food("Dumplings w/ Meat", 20, FoodCategoryEnum.DUMPLINGS_PIEROGI),
                new Food("Russian Pierogi", 15, FoodCategoryEnum.DUMPLINGS_PIEROGI),
                new Food("Pierogi w/ Strawberries", 20, FoodCategoryEnum.DUMPLINGS_PIEROGI),
                new Food("Small Dumplings w/ Meat", 15, FoodCategoryEnum.DUMPLINGS_PIEROGI)));

        map.put(FoodCategoryEnum.SIDE_DISHES, Arrays.asList(
                new Food("Potatoes", 10, FoodCategoryEnum.SIDE_DISHES),
                new Food("French Fries", 10, FoodCategoryEnum.SIDE_DISHES),
                new Food("Salad", 8, FoodCategoryEnum.SIDE_DISHES),
                new Food("Chicken Salad", 10, FoodCategoryEnum.SIDE_DISHES)));

        return map;
    }
}