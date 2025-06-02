package GUI;

import GUI.cook.OrderedFoodWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class CookGUI extends BaseGUI implements ActionListener {

    private JButton orderedFoodButton,goBackButton;
    public CookGUI(String title, Socket socket) {
        super(title,socket);
        init();
    }
 //  👩🏻‍🍳
 @Override
 protected void init() {
     Container WaiterContainer = getContentPane();
     WaiterContainer.setLayout(new BorderLayout());
     setSize(400, 250);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(EXIT_ON_CLOSE);
     setResizable(false);

     JLabel titleLabel = new JLabel("Cook Menu");
     titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
     titleLabel.setForeground(Color.WHITE);
     titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

     JPanel titlePanel = new JPanel(new BorderLayout());
     titlePanel.setBackground(Color.DARK_GRAY);
     titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
     titlePanel.add(titleLabel, BorderLayout.CENTER);

     orderedFoodButton = new JButton("Ordered Food");
     goBackButton = new JButton("Go Back");

     Font buttonFont = new Font("Serif", Font.BOLD, 18);
     orderedFoodButton.setFont(buttonFont);
     goBackButton.setFont(buttonFont);

     JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
     buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
     buttonPanel.setBackground(Color.DARK_GRAY);
     buttonPanel.add(orderedFoodButton);
     buttonPanel.add(goBackButton);

     WaiterContainer.add(titlePanel, BorderLayout.NORTH);
     WaiterContainer.add(buttonPanel, BorderLayout.CENTER);
     styleButton(orderedFoodButton);
     styleButton(goBackButton);
     orderedFoodButton.addActionListener(this);
     goBackButton.addActionListener(this);


     setVisible(true);
 }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == orderedFoodButton) {
            new OrderedFoodWindow(this, objectOut, objectIn);
        } else if (source == goBackButton) {
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
