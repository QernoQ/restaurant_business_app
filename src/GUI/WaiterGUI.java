package GUI;

import GUI.waiter.AddBillWindow;
import GUI.waiter.CurrentBillWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WaiterGUI extends BaseGUI implements ActionListener {

    private JButton AddBill, CurrentBill,goBackButton;

    public WaiterGUI(String title, Socket socket) throws IOException {
        super(title, socket);
        init();
    }

    @Override
    protected void init() {
        Container WaiterContainer = getContentPane();
        WaiterContainer.setLayout(new BorderLayout());
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
        CurrentBill = new JButton("Current Bills");
        goBackButton = new JButton("Go Back");

        Font buttonFont = new Font("Serif", Font.BOLD, 18);
        AddBill.setFont(buttonFont);
        CurrentBill.setFont(buttonFont);
        goBackButton.setFont(buttonFont);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(AddBill);
        buttonPanel.add(CurrentBill);
        buttonPanel.add(goBackButton);
        AddBill.addActionListener(this);
        CurrentBill.addActionListener(this);
        goBackButton.addActionListener(this);

        WaiterContainer.add(titlePanel, BorderLayout.NORTH);
        WaiterContainer.add(buttonPanel, BorderLayout.CENTER);
        styleButton(CurrentBill);
        styleButton(AddBill);
        styleButton(goBackButton);


        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == AddBill) {
            new AddBillWindow(this,objectOut,objectIn,true,false);

        } else if (src == CurrentBill) {
            new CurrentBillWindow(this,objectOut,objectIn);

        } else if (src == goBackButton) {
            dispose();
            try {
                socket = new Socket(serverIP,serverPort);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            new ClientGUI("Restaurant Management Application",socket);
        }

    }
}
