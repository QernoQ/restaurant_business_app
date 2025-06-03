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

public class ManageBillWindow extends JDialog implements ActionListener {
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private List<Food> foodList;
    private JPanel mainPanel, foodListPanel, buttonPanel;
    private WaiterGUI waiterGUI;
    private JButton backButton, closeButton, addItemButtton;
    private int billID;
    private JButton billButton;

    private AddBillWindow addBillWindow;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == closeButton) {
                objectOut.writeObject(SortEnum.CLOSEBILL);
                objectOut.flush();
                objectOut.writeObject(new Bill(false, false, foodList, billID));
                objectOut.flush();
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
