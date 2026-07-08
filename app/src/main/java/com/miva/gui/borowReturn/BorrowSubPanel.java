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
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.miva.controller.LibraryManager;
import com.miva.model.LibraryItem;
import com.miva.model.UserAccount;

public class BorrowSubPanel extends JPanel {
    private final LibraryManager manager;
    private JTextField txtUserId, txtItemId;
    private JComboBox<UserAccount> userComboAction;
    private JComboBox<LibraryItem> itemComboAction;
    private List<UserAccount> users;
    private List<LibraryItem> catalog;

    public BorrowSubPanel(LibraryManager manager) {
        this.manager = manager;

        // Container Alignment (BoxLayout vertical layout requirement compliance)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        this.setBackground(new Color(248, 249, 250));

        // 1. HEADER TITLE SECTION
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

        // Row 0: Select User
        users = manager.getUserManager().getAllUsers();
        catalog = manager.getCatalogue();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Select User:"), gbc);

        // FIX: Convert the List into an Array inside the JComboBox constructor
        userComboAction = new JComboBox<UserAccount>(users.toArray(new UserAccount[0]));
        userComboAction.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formCard.add(userComboAction, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Select Catalog Item:"), gbc);

        // FIX: Convert the List into an Array inside the JComboBox constructor
        itemComboAction = new JComboBox<LibraryItem>(catalog.toArray(new LibraryItem[0]));
        itemComboAction.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        formCard.add(itemComboAction, gbc);

        // Row 3: Action Trigger Button Configuration
        JButton btnSubmit = new JButton("Process Transaction");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSubmit.setBackground(new Color(56, 189, 248)); // Matches our Premium Blue Accent Palette
        btnSubmit.setForeground(new Color(15, 23, 42));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.7;
        formCard.add(btnSubmit, gbc);

        this.add(formCard);

        // Map operational callback trigger
        btnSubmit.addActionListener(e -> executeCirculationPipeline());

    }

    /**
     * Requirement: Interactive Circulation Engine with comprehensive error
     * validation handling
     */
    private void executeCirculationPipeline() {
        String userId = txtUserId.getText().trim().toUpperCase();
        String itemId = txtItemId.getText().trim().toUpperCase();
        String chosenAction = (String) userComboAction.getSelectedItem();

        // Check 1: Mandatory parameter validation check
        if (userId.isEmpty() || itemId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Transaction Denied:\nPlease verify both Account ID and Item ID fields are populated.",
                    "Missing Parameters", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check 2: Verify user registry records exist inside local file caches
        Optional<UserAccount> userOpt = manager.getUserDatabase().getUserById(userId);
        if (userOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Transaction Denied:\nNo registered library member found matching ID: " + userId,
                    "Account Conflict", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UserAccount user = userOpt.get();

        // Check 3: Verify item records exist in inventory mapping indices
        // We look up the item matching the ID out of our catalogue array collection
        LibraryItem targetItem = manager.getCatalogue().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (targetItem == null) {
            JOptionPane.showMessageDialog(this,
                    "Transaction Denied:\nNo material found in active registry matching ID: " + itemId,
                    "Asset Catalog Conflict", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. CORE BUSINESS CIRCUITS LOGIC
        if ("Borrow Item".equals(chosenAction)) {
            // Check 4: Handle inventory conflict conditions
            if (!targetItem.isAvailable()) {
                JOptionPane.showMessageDialog(this,
                        "Operation Interrupted:\n\"" + targetItem.getTitle()
                                + "\" is already checked out to another user account.",
                        "Availability Alert", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update item availability state attributes via its interface methods
            boolean checkoutApproved = targetItem.borrowItem(userId);
            if (checkoutApproved) {
                targetItem.setAvailable(false);
                // Append log event tracking codes right down inside member history collections
                manager.getUserDatabase().logBorrowAction(userId, itemId);

                // FORCE RESYNC: Update the central cache state registry and commit immediately
                // to json files
                manager.addItem(targetItem);

                JOptionPane
                        .showMessageDialog(this,
                                "Success:\n\"" + targetItem.getTitle() + "\" successfully checked out to "
                                        + user.getName() + ".",
                                "Transaction Approved", JOptionPane.INFORMATION_MESSAGE);
                clearInputs();
            }

        } else {
            // PROCESSING THE RETURN OPTION TRACK
            if (targetItem.isAvailable()) {
                JOptionPane.showMessageDialog(this,
                        "Operation Canceled:\n\"" + targetItem.getTitle()
                                + "\" is already marked as available on shelves.",
                        "Redundant Action", JOptionPane.WARNING_MESSAGE);
                return;
            }

            targetItem.returnItem();
            targetItem.setAvailable(true);

            // Re-save updated element states forcing serialization update loops
            manager.addItem(targetItem);

            JOptionPane.showMessageDialog(this,
                    "Success:\n\"" + targetItem.getTitle() + "\" successfully checked back into storage arrays.",
                    "Inventory Return Complete", JOptionPane.INFORMATION_MESSAGE);
            clearInputs();
        }
    }

    private void clearInputs() {
        txtUserId.setText("");
        txtItemId.setText("");
        userComboAction.setSelectedIndex(0);
        itemComboAction.setSelectedIndex(0);
        txtUserId.requestFocus();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
}
