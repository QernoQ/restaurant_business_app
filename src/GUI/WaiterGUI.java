package GUI;

import GUI.waiter.AddBillWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WaiterGUI extends BaseGUI implements ActionListener {

    protected ObjectOutputStream objectOut;
    protected ObjectInputStream objectIn;

    private JButton AddBill,ManageBill;

    public WaiterGUI(String title, Socket socket) throws IOException {
        super(title, socket);
        this.objectOut = new ObjectOutputStream(socket.getOutputStream());
        this.objectIn = new ObjectInputStream(socket.getInputStream());
        init();
    }

    @Override
    protected void init() {
        Container BossContainer = getContentPane();
        BossContainer.setLayout(new BorderLayout());
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JLabel titleLabel = new JLabel("Waiter Menu");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.DARK_GRAY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        AddBill = new JButton("Add Bill");
        ManageBill = new JButton("Manage Bills");

        Font buttonFont = new Font("Serif", Font.BOLD, 18);
        AddBill.setFont(buttonFont);
        ManageBill.setFont(buttonFont);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(AddBill);
        buttonPanel.add(ManageBill);
        AddBill.addActionListener(this);
        ManageBill.addActionListener(this);

        BossContainer.add(titlePanel, BorderLayout.NORTH);
        BossContainer.add(buttonPanel, BorderLayout.CENTER);
        styleButton(ManageBill);
        styleButton(AddBill);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == AddBill) {
            new AddBillWindow(WaiterGUI.this,this,objectOut,objectIn);

        } else if (src == ManageBill) {

        }

    }
}
