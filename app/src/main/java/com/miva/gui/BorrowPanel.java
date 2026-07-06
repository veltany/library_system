// BorrowPanel.java
package com.miva.gui;
import com.miva.controller.BorrowController;
import java.awt.*;
import javax.swing.*;

public class BorrowPanel extends JPanel {
    private BorrowController borrowController;
    private JTextField txtItemId;
    private JTextField txtUserId;
    private JTextArea txtStatus;

    public BorrowPanel(BorrowController controller) {
        this.borrowController = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Item ID:"));
        txtItemId = new JTextField();
        formPanel.add(txtItemId);
        
        formPanel.add(new JLabel("User ID:"));
        txtUserId = new JTextField();
        formPanel.add(txtUserId);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnBorrow = new JButton("Borrow Item");
        JButton btnReturn = new JButton("Return Item");
        buttonPanel.add(btnBorrow);
        buttonPanel.add(btnReturn);

        // Status Area
        txtStatus = new JTextArea(5, 30);
        txtStatus.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtStatus);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Event Listeners
        btnBorrow.addActionListener(e -> handleBorrow());
        btnReturn.addActionListener(e -> handleReturn());
    }

    private void handleBorrow() {
        String itemId = txtItemId.getText().trim();
        String userId = txtUserId.getText().trim();
        if (itemId.isEmpty() || userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String result = borrowController.processBorrow(itemId, userId);
        txtStatus.append(result + "\n");
    }

    private void handleReturn() {
        String itemId = txtItemId.getText().trim();
        if (itemId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Item ID", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String result = borrowController.processReturn(itemId);
        txtStatus.append(result + "\n");
    }
}