package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class CookGUI extends BaseGUI {

    private JButton orderedFood;
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

     JLabel titleLabel = new JLabel("Waiter Menu");
     titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
     titleLabel.setForeground(Color.WHITE);
     titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

     JPanel titlePanel = new JPanel(new BorderLayout());
     titlePanel.setBackground(Color.DARK_GRAY);
     titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
     titlePanel.add(titleLabel, BorderLayout.CENTER);

     orderedFood = new JButton("Ordered Food");

     Font buttonFont = new Font("Serif", Font.BOLD, 18);
     orderedFood.setFont(buttonFont);

     JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
     buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
     buttonPanel.setBackground(Color.DARK_GRAY);
     buttonPanel.add(orderedFood);

     WaiterContainer.add(titlePanel, BorderLayout.NORTH);
     WaiterContainer.add(buttonPanel, BorderLayout.CENTER);
     styleButton(orderedFood);
     styleButton(orderedFood);


     setVisible(true);
 }

}
