package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class BossGUI extends BaseGUI implements ActionListener {
    JButton AddWorker, RemoveWorker;

    public BossGUI(String title, Socket socket) {
        super(title, socket);
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
        close();

        JLabel titleLabel = new JLabel("Boss Menu");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.DARK_GRAY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        AddWorker = new JButton("Add Worker");
        RemoveWorker = new JButton("Remove Worker");

        Font buttonFont = new Font("Serif", Font.BOLD, 18);
        AddWorker.setFont(buttonFont);
        RemoveWorker.setFont(buttonFont);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        buttonPanel.setBackground(Color.DARK_GRAY);
        buttonPanel.add(AddWorker);
        buttonPanel.add(RemoveWorker);

        // ---------- Add to Container ----------
        BossContainer.add(titlePanel, BorderLayout.NORTH);
        BossContainer.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }


    protected void close() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (out != null) {
                    out.println(getTitle() + " Disconnected from server");
                }
                dispose();
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == AddWorker) {
            out.println("Worker Addded");

        } else if (source == RemoveWorker) {
            out.println("Worker Removed");

        }
    }
}
