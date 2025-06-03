package GUI.waiter;

import GUI.WaiterGUI;
import GUI.cook.OrderedFoodWindow;
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


public class CurrentBillWindow extends JDialog {
    private Timer refreshTimer;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private WaiterGUI waiterGUI;
    private JPanel mainPanel, buttonPanel;
    private List<Food> foodList;
    private List<Bill> billRecived;
    private int foodID;
    private AddBillWindow addBillWindow;

    public CurrentBillWindow(JFrame parent, ObjectOutputStream objectOut, ObjectInputStream objectIn) {
        super(parent, "Add Bill", true);
        this.objectOut = objectOut;
        this.objectIn = objectIn;
        this.waiterGUI = (WaiterGUI) parent;
        init(parent);
    }

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
            public void windowOpened(WindowEvent e) {
                refreshOrders(); // na start
                refreshTimer = new Timer(5000, evt -> refreshOrders()); // co 5 sek
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



