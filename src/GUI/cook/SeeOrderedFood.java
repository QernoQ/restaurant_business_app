package GUI.cook;

import GUI.CookGUI;
import model.Bill;
import model.Food;
import model.FoodCategoryEnum;
import model.SortEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SeeOrderedFood extends JDialog implements ActionListener {

    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private JPanel mainPanel, foodListPanel, buttonPanel;
    private CookGUI cookGUI;
    private JButton closeButton, removeOrderButton, refreshButton;
    private int billID;
    private JButton orderButton;

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
        refreshButton = new JButton("Odśwież");

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
            JOptionPane.showMessageDialog(this, "Błąd podczas odświeżania: " + ex.getMessage(),
                    "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

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
