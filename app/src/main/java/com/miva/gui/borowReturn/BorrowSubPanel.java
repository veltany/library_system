package com.miva.gui.borowReturn;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.miva.controller.BorrowController;
import com.miva.controller.LibraryManager;
import com.miva.model.LibraryItem;
import com.miva.model.UserAccount;
import com.miva.utils.Response;

public class BorrowSubPanel extends JPanel {
    private final LibraryManager manager;
    private JComboBox<UserAccount> userComboAction;
    private JComboBox<LibraryItem> itemComboAction;
    private List<UserAccount> users;
    private List<LibraryItem> catalog;
    private BorrowController borrowController;

    public BorrowSubPanel(LibraryManager manager) {
        this.manager = manager;
        this.borrowController = new BorrowController(manager);

        // Container Alignment (BoxLayout vertical layout requirement compliance)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        this.setBackground(new Color(248, 249, 250));

        // HEADER TITLE SECTION
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setBackground(null);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Borrow Item");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblSubtitle = new JLabel("Process resource checkout.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(127, 140, 141));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        this.add(headerPanel);
        this.add(Box.createVerticalStrut(20));

        // 2. TRANSACTION FORM CARD (GridBagLayout compliance)
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 233, 240), 1, true),
                new EmptyBorder(25, 25, 25, 25)));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.setMaximumSize(new Dimension(600, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fetch data initially
        users = manager.getUserManager().getAllUsers();
        catalog = manager.getAvailableItems();

        // select User Form
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Select User:"), gbc);

        userComboAction = new JComboBox<UserAccount>(users.toArray(new UserAccount[0]));
        userComboAction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formCard.add(userComboAction, gbc);

        // Select Item Form
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Select Catalog Item:"), gbc);
        itemComboAction = new JComboBox<LibraryItem>(catalog.toArray(new LibraryItem[0]));
        itemComboAction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formCard.add(itemComboAction, gbc);

        // Action Trigger Button Configuration
        JButton btnSubmit = new JButton("Borrow Item");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSubmit.setBackground(new Color(56, 189, 248));
        btnSubmit.setForeground(new Color(15, 23, 42));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        formCard.add(btnSubmit, gbc);
        // Map operational callback trigger
        btnSubmit.addActionListener(e -> executeBorrowProcess());

        this.add(formCard);
    }

    /**
     * Requirement: Interactive borrow engine with comprehensive error
     * validation handling
     */
    private void executeBorrowProcess() {
        UserAccount selectedUser = (UserAccount) userComboAction.getSelectedItem();
        LibraryItem selectedItem = (LibraryItem) itemComboAction.getSelectedItem();

        // validate input
        if (selectedUser == null || selectedItem == null) {
            JOptionPane.showMessageDialog(this,
                    "Borrow Denied:\nPlease make sure both fields are selected.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // extract ids
        String userId = selectedUser.getUserId();
        String itemId = selectedItem.getId();

        // Process borrow request
        Response<Void> result = borrowController.processBorrow(itemId, userId);

        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    result.getMessage(), // Updated to use encapsulation getter method
                    "Transaction Approved", JOptionPane.INFORMATION_MESSAGE);

            // FIX 2: Refresh the dropdown listings directly from backend database caches
            refreshComboBoxData();
            clearInputs();
        } else {
            JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Refresh data matrices and force refreshes UI ComboBox
     * models
     */
    public void refreshComboBoxData() {
        // Fetch up-to-date information
        users = manager.getUserManager().getAllUsers();
        catalog = manager.getAvailableItems();

        // Overwrite internal model
        userComboAction.setModel(new DefaultComboBoxModel<>(users.toArray(new UserAccount[0])));
        itemComboAction.setModel(new DefaultComboBoxModel<>(catalog.toArray(new LibraryItem[0])));

        // Notify rendering paths to redraw interface
        this.revalidate();
        this.repaint();
    }

    private void clearInputs() {
        if (userComboAction.getItemCount() > 0) {
            userComboAction.setSelectedIndex(0);
        }
        if (itemComboAction.getItemCount() > 0) {
            itemComboAction.setSelectedIndex(0);
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
}
