package com.miva.gui.users;

import com.miva.controller.UserManager;
import com.miva.model.UserAccount;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class UsersPanel extends JPanel {
    private final UserManager userManager;
    private final UserManagementHub hub;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public UsersPanel(UserManager userManager, UserManagementHub hub) {
        this.userManager = userManager;
        this.hub = hub;
        this.setLayout(new BorderLayout(0, 15));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setBackground(Color.WHITE);

        // 1. Top Panel Layout Row: Search and Action Header Row
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(null);

        txtSearch = new JTextField("Search members by name or ID...", 20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnSearch = new JButton("🔍 Filter");
        btnSearch.addActionListener(e -> refreshUserGrid());

        JPanel searchBarBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchBarBox.add(txtSearch);
        searchBarBox.add(btnSearch);

        JButton btnNewUser = new JButton("➕ Create Member Profile");
        btnNewUser.setBackground(new Color(46, 204, 113));
        btnNewUser.setForeground(Color.WHITE);
        btnNewUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNewUser.addActionListener(e -> hub.openProfileView("")); // Open profile view in blank creation mode

        topPanel.add(searchBarBox, BorderLayout.WEST);
        topPanel.add(btnNewUser, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);

        // 2. Center Panel: Core Table View
        String[] columns = {"Member Account ID", "Full Name", "Total Borrowed History Count"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        this.add(scroll, BorderLayout.CENTER);

        // 3. Bottom Panel Layout Row: Manage/View Selected Actions Row
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottomPanel.setBackground(null);

        JButton btnManage = new JButton("🔎 View & Edit Selected Profile");
        btnManage.setBackground(new Color(52, 152, 219));
        btnManage.setForeground(Color.WHITE);
        btnManage.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        btnManage.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please highlight a member row in the table first.", "Selection Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String selectedId = table.getValueAt(selectedRow, 0).toString();
            hub.openProfileView(selectedId);
        });

        bottomPanel.add(btnManage);
        this.add(bottomPanel, BorderLayout.SOUTH);

        refreshUserGrid();
    }

    public void refreshUserGrid() {
        tableModel.setRowCount(0);
        String query = txtSearch.getText().trim().toLowerCase();
        boolean isPlaceholder = query.contains("search members");

        List<UserAccount> filteredList = userManager.getAllUsers().stream()
            .filter(u -> isPlaceholder || u.getUserId().toLowerCase().contains(query) || u.getName().toLowerCase().contains(query))
            .collect(Collectors.toList());

        for (UserAccount user : filteredList) {
            Object[] row = {
                user.getUserId(),
                user.getName(),
                user.getBorrowingHistory() != null ? user.getBorrowingHistory().size() : 0
            };
            tableModel.addRow(row);
        }
    }
}
