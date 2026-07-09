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
import javax.swing.DefaultComboBoxModel; // Added dependency mapping
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.miva.controller.BorrowController;
import com.miva.controller.LibraryManager;
import com.miva.model.LibraryItem;
import com.miva.utils.Response;

public class ReturnSubPanel extends JPanel {
    private final LibraryManager manager;
    private JComboBox<LibraryItem> itemComboAction;
    private List<LibraryItem> catalog;
    private BorrowController borrowController;

    public ReturnSubPanel(LibraryManager manager) {
        this.manager = manager;
        this.borrowController = new BorrowController(manager);
        this.catalog = manager.getBorrowedItems();

        // Container Alignment (BoxLayout vertical layout requirement compliance)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        this.setBackground(new Color(248, 249, 250));

        // 1. HEADER TITLE SECTION
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setBackground(null);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Return Item");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblSubtitle = new JLabel("Process resource checkout tokens and manage real-time inventory circulation.");
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
        formCard.setMaximumSize(new Dimension(600, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Operation Action Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Select Catalog Item:"), gbc);

        itemComboAction = new JComboBox<>(catalog.toArray(new LibraryItem[0]));
        itemComboAction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formCard.add(itemComboAction, gbc);

        // Row 1: Action Trigger Button Configuration
        JButton btnSubmit = new JButton("Return Item");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSubmit.setBackground(new Color(56, 189, 248));
        btnSubmit.setForeground(new Color(15, 23, 42));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formCard.add(btnSubmit, gbc);

        this.add(formCard);

        // Map operational callback trigger
        btnSubmit.addActionListener(e -> processReturnFlow());
    }

    /**
     * Requirement: Interactive Circulation Engine with comprehensive error
     * validation handling
     */
    private void processReturnFlow() {
        LibraryItem selectedItem = (LibraryItem) itemComboAction.getSelectedItem();

        // Defensive validation against empty/null values
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this,
                    "Return Denied:\nPlease select an item to proceed.",
                    "Missing Parameters", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemId = selectedItem.getId();
        Response<Void> result = borrowController.processReturn(itemId);

        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Transaction Approved", JOptionPane.INFORMATION_MESSAGE);

            // FIX: Force reload data from backend and apply to model binding
            refreshComboBoxData();
            clearInputs();
        } else {
            JOptionPane.showMessageDialog(this,
                    result.getMessage(),
                    "Transaction Denied", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Re-queries database layer data matrices and force refreshes UI ComboBox model
     */
    public void refreshComboBoxData() {
        // Fetch up-to-date items that are currently marked as borrowed
        catalog = manager.getBorrowedItems();

        // Overwrite internal model bindings with fresh payload elements
        itemComboAction.setModel(new DefaultComboBoxModel<>(catalog.toArray(new LibraryItem[0])));

        // Notify rendering paths to redraw interface artifacts safely
        this.revalidate();
        this.repaint();
    }

    private void clearInputs() {
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
