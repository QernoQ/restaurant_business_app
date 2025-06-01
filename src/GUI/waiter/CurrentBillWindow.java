package GUI.waiter;

import GUI.WaiterGUI;
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
        setTitle("Add Bill");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        mainPanel = new JPanel(new GridLayout(2, 2));
        mainPanel.setBackground(new Color(60, 60, 60));

        JScrollPane menuScroll = new JScrollPane(mainPanel);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        setLocationRelativeTo(parent);
        addWindowListener(new WindowAdapter() {
            boolean active = false;
            public void windowOpened(WindowEvent e) {
                try {
                    objectOut.writeObject(SortEnum.READBILL);
                    objectOut.flush();
                    billRecived = (List<Bill>) objectIn.readObject();
                    for (Bill bill : billRecived) {
                        if(bill.isActive()) {
                            addButton(bill);
                            active = true;
                        }
                    }
                    if(!active) {
                        dispose();
                        JOptionPane.showMessageDialog(CurrentBillWindow.this,"No active bills!");
                    }
                    revalidate();
                    repaint();
                    pack();
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(CurrentBillWindow.this, ex.getMessage());
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
            foodList = bill.getCurrentOrder();
            foodID = bill.getBillId();

            new ManageBillWindow(this,waiterGUI,objectOut, objectIn, foodList, foodID,button,addBillWindow = new AddBillWindow(waiterGUI, objectOut, objectIn,false,true));
        });
    }

}

