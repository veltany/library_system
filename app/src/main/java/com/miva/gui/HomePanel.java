package com.miva.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.miva.controller.LibraryManager;
import com.miva.gui.items.AddItemDialog;

public class HomePanel extends JPanel {
    private final LibraryManager manager;

    // UI Metric Components
    private JLabel lblBooksCount, lblMagCount, lblJournalCount, lblUsersCount, lblBorrowedCount, lblAvailCount;
    private JTextField txtSearch;

    public HomePanel(LibraryManager manager, MainWindow mainFrame) {
        this.manager = manager;

        // 1. Root structural framework
        this.setLayout(new BorderLayout(0, 20));
        this.setBorder(new EmptyBorder(25, 25, 25, 25));
        this.setBackground(new Color(245, 246, 250)); // Clean off-white background

        // 2. BUILD HEADER: Reusable Search Bar + Greeting
        this.add(createHeaderPanel(), BorderLayout.NORTH);

        // 3. BUILD CENTER: Library Metrics Dashboard Matrix Grid
        this.add(createMetricsDashboard(), BorderLayout.CENTER);

        // 4. BUILD FOOTER: Administrative Quick Action Buttons Panel
        this.add(createActionFooter(mainFrame), BorderLayout.SOUTH);

        // Initial live sync computing cycle
        refreshMetrics();
    }

    /**
     * Requirement: Reusable Search Bar Component Frame
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(null); // Inherit parental canvas background tint

        // Welcome Branding Sub-labels
        JPanel textGroup = new JPanel(new GridLayout(2, 1, 2, 2));
        textGroup.setBackground(null);
        JLabel title = new JLabel("System Overview Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        JLabel subtitle = new JLabel("Real-time telemetry tracking resource metrics and catalog configurations.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(127, 140, 141));
        textGroup.add(title);
        textGroup.add(subtitle);

        // REUSABLE SEARCH INPUT GROUP
        JPanel searchGroup = new JPanel(new BorderLayout(5, 0));
        searchGroup.setBackground(Color.WHITE);
        searchGroup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 214, 229), 1, true),
                new EmptyBorder(6, 12, 6, 12)));

        JLabel searchIcon = new JLabel("🔍 ");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtSearch = new JTextField("Search unified catalog items...", 22);
        txtSearch.setBorder(null); // Flat UI design
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setForeground(Color.GRAY);

        // Dynamic watermarking focus clearance handlers
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().equals("Search unified catalog items...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Search unified catalog items...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        searchGroup.add(searchIcon, BorderLayout.WEST);
        searchGroup.add(txtSearch, BorderLayout.CENTER);

        // Keep search group vertically centered
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setBackground(null);
        rightWrapper.add(searchGroup);

        headerPanel.add(textGroup, BorderLayout.WEST);
        headerPanel.add(rightWrapper, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Requirement Check: Grid Layout matrix building interactive telemetry tracking
     * blocks
     */
    private JPanel createMetricsDashboard() {
        // 3 columns, 2 rows for balanced telemetry allocation spacing maps
        JPanel dashboardGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        dashboardGrid.setBackground(null);

        // Initialize display monitoring strings
        lblBooksCount = new JLabel("0", SwingConstants.CENTER);
        lblMagCount = new JLabel("0", SwingConstants.CENTER);
        lblJournalCount = new JLabel("0", SwingConstants.CENTER);
        lblUsersCount = new JLabel("0", SwingConstants.CENTER);
        lblBorrowedCount = new JLabel("0", SwingConstants.CENTER);
        lblAvailCount = new JLabel("0", SwingConstants.CENTER);

        // Add modular styled dashboard metric cards
        dashboardGrid.add(createStatCard("📘 Total Books Stocked", lblBooksCount, new Color(52, 152, 219)));
        dashboardGrid.add(createStatCard("📰 Magazines Tracked", lblMagCount, new Color(155, 89, 182)));
        dashboardGrid.add(createStatCard("🔬 Academic Journals", lblJournalCount, new Color(26, 188, 156)));
        dashboardGrid.add(createStatCard("👥 Registered Members", lblUsersCount, new Color(52, 73, 94)));
        dashboardGrid.add(createStatCard("🔄 Active Borrowed Items", lblBorrowedCount, new Color(231, 76, 60)));
        dashboardGrid.add(createStatCard("✅ Available in Shelves", lblAvailCount, new Color(46, 204, 113)));

        return dashboardGrid;
    }

    /**
     * Helper block styling the visual look of individual metrics dashboards card
     * items
     */
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 233, 240), 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        // Subtle accent line banner on top of the card
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(10, 4));
        card.add(accentBar, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(127, 140, 141));
        card.add(lblTitle, BorderLayout.CENTER);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(new Color(44, 62, 80));
        card.add(valueLabel, BorderLayout.SOUTH);

        return card;
    }

    /**
     * Footer Buttons: Requirement checklist integration handling Add, Import, and
     * Export configurations
     */
    private JPanel createActionFooter(MainWindow mainFrame) {
        JPanel footerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        footerRow.setBackground(null);

        JButton btnAdd = createStyledButton("➕ Add New Item", new Color(46, 204, 113));
        JButton btnImport = createStyledButton("📥 Import Library Data", new Color(52, 152, 219));
        JButton btnExport = createStyledButton("📤 Export Data (Backup)", new Color(52, 73, 94));

        // BUTTON INTERACTION TRIGGERS
        btnAdd.addActionListener(e -> {
            // 2. Launch add new dialog pop up
            AddItemDialog popDialog = new AddItemDialog(mainFrame, manager);
            popDialog.setVisible(true); // Blocks background execution threads until dismissed
        });

        // Action 2 & 3 Placeholder callbacks: We will bind standard file selection
        // boxes here shortly
        btnImport.addActionListener(
                e -> JOptionPane.showMessageDialog(this, "Import File Chooser window module coming up next!"));
        btnExport.addActionListener(
                e -> JOptionPane.showMessageDialog(this, "Export Save File system pipeline coming up next!"));

        footerRow.add(btnAdd);
        footerRow.add(btnImport);
        footerRow.add(btnExport);

        return footerRow;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(10, 20, 10, 20));
        return btn;
    }

    /**
     * ReFetches real-time counts from the local JSON database map
     */
    public void refreshMetrics() {
        int books = 0, magazines = 0, journals = 0, borrowed = 0, available = 0;

        // Loop over local records to calculate live values
        for (var item : manager.getCatalogue()) {
            switch (item.getItemType()) {
                case "Book" -> books++;
                case "Magazine" -> magazines++;
                case "Journal" -> journals++;
            }
            if (item.isAvailable()) {
                available++;
            } else {
                borrowed++;
            }
        }

        int totalUsers = manager.getUserDatabase().getAllUsers().size();

        // Feed calculated fields into view text frames safely
        lblBooksCount.setText(String.valueOf(books));
        lblMagCount.setText(String.valueOf(magazines));
        lblJournalCount.setText(String.valueOf(journals));
        lblUsersCount.setText(String.valueOf(totalUsers));
        lblBorrowedCount.setText(String.valueOf(borrowed));
        lblAvailCount.setText(String.valueOf(available));
    }
}
