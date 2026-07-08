package com.miva.gui.users;

import com.miva.controller.UserManager;
import com.miva.model.UserAccount;
import com.miva.utils.IDGenerator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Optional;

public class SingleUserPanel extends JPanel {
    private final UserManager userManager;
    private final UserManagementHub hub;
    private JTextField txtId, txtName;
    private DefaultListModel<String> historyListModel;
    private JButton btnDelete, btnSave;
    private boolean isEditMode = false;

    public SingleUserPanel(UserManager userManager, UserManagementHub hub) {
        this.userManager = userManager;
        this.hub = hub;
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.WHITE);

        // 1. Back Header Action Bar
        JButton btnBack = new JButton("⬅ Back to Member Registry List");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.addActionListener(e -> hub.showListCard());
        this.add(btnBack, BorderLayout.NORTH);

        // 2. Left Column Layout Block: Input Profile Data Card
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(new Color(248, 249, 250));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 233, 240)), new EmptyBorder(20, 20, 20, 20)
        ));
        formCard.setPreferredSize(new Dimension(400, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(new JLabel("Member Account ID:"), gbc);
        txtId = new JTextField(12);
        txtId.setEditable(false); // ID generation happens automatically
        gbc.gridx = 1;
        formCard.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(new JLabel("Full Name:"), gbc);
        txtName = new JTextField(15);
        gbc.gridx = 1;
        formCard.add(txtName, gbc);

        // Action Modification Buttons
        btnSave = new JButton("Save Record");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> commitProfileMutation());

        btnDelete = new JButton("❌ Delete Profile");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> executeDeleteAction());

        JPanel formActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formActions.setBackground(null);
        formActions.add(btnSave);
        formActions.add(btnDelete);

        gbc.gridx = 1; gbc.gridy = 2;
        formCard.add(formActions, gbc);
        this.add(formCard, BorderLayout.WEST);

        // 3. Right Column Layout Block: Circulation Log History Tracking Grid
        JPanel historyCard = new JPanel(new BorderLayout(0, 10));
        historyCard.setBackground(Color.WHITE);
        historyCard.setBorder(BorderFactory.createTitledBorder("Resource Checkout History Log"));

        historyListModel = new DefaultListModel<>();
        JList<String> historyJList = new JList<>(historyListModel);
        historyJList.setFont(new Font("Consolas", Font.PLAIN, 12));
        historyCard.add(new JScrollPane(historyJList), BorderLayout.CENTER);

        this.add(historyCard, BorderLayout.CENTER);
    }

    public void loadProfileDetails(String targetId) {
        historyListModel.clear();
        if (targetId.isEmpty()) {
            // Setup fresh instantiation creation parameters
            isEditMode = false;
            txtId.setText(IDGenerator.generateUniqueID());
            txtName.setText("");
            btnDelete.setVisible(false);
            btnSave.setText("Register Account");
        } else {
            // Load an existing record profile from file storage
            isEditMode = true;
            btnDelete.setVisible(true);
            btnSave.setText("Update Record");
            
            Optional<UserAccount> userOpt = userManager.getUserById(targetId);
            if (userOpt.isPresent()) {
                UserAccount user = userOpt.get();
                txtId.setText(user.getUserId());
                txtName.setText(user.getName());
                
                if (user.getBorrowingHistory() != null && !user.getBorrowingHistory().isEmpty()) {
                    for (String itemId : user.getBorrowingHistory()) {
                        historyListModel.addElement("Checked out Asset Tracking Key ID: [" + itemId + "]");
                    }
                } else {
                    historyListModel.addElement("No recorded library resource borrowings found.");
                }
            }
        }
    }

    private void commitProfileMutation() {
        String name = txtName.getText().trim();
        String id = txtId.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Profile Rejected: Full Name cannot be left blank.", "Validation Alert", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserAccount userProfile = new UserAccount(id, name);
        if (isEditMode) {
            // Preserve history list references across mutations
            userManager.getUserById(id).ifPresent(old -> {
                if (old.getBorrowingHistory() != null) {
                    old.getBorrowingHistory().forEach(userProfile::addToHistory);
                }
            });
            userManager.updateUser(userProfile);
            JOptionPane.showMessageDialog(this, "Member records modified successfully.");
        } else {
            userManager.createUser(userProfile);
            JOptionPane.showMessageDialog(this, "New member registered successfully! Generated ID: " + id);
        }
        hub.showListCard();
    }

    private void executeDeleteAction() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently erase this member account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            userManager.deleteUser(txtId.getText());
            hub.showListCard();
        }
    }
}
