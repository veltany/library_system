package com.miva.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.miva.controller.LibraryManager;
import com.miva.gui.borowReturn.BorrowSubPanel;
import com.miva.gui.borowReturn.ReturnSubPanel;

public class BorrowPanel extends JPanel {
    private final LibraryManager manager;
    private JTextField txtUserId, txtItemId;
    private JComboBox<String> comboAction;

    private BorrowSubPanel borrowPanel;
    private ReturnSubPanel returnPanel;

    public BorrowPanel(LibraryManager manager) {
        this.manager = manager;

        // Container Layout: Enforces vertical stacking order
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        this.setBackground(new Color(248, 249, 250));

        // 1. HEADER TITLE SECTION ( Left-Align)

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(null);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Circulation Management Desk");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel(
                "Process library resource borrow and return in real-time inventory circulation.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(127, 140, 141));
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(lblTitle);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblSubtitle);

        this.add(headerPanel);
        this.add(Box.createVerticalStrut(25));

        // EQUALLY DISTRIBUTED TABS SECTION

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.borrowPanel = new BorrowSubPanel(manager);
        this.returnPanel = new ReturnSubPanel(manager);

        tabbedPane.addTab("Borrow", borrowPanel);
        tabbedPane.addTab("Return", returnPanel);

        // Custom renderer to span tabs equally across the full width
        configureEqualWidthTabs(tabbedPane);

        // TAB CHANGE LISTENER FOR REFRESHES

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();

            if (selectedIndex == 0) {
                // If switching back onto the "Borrow" view index
                borrowPanel.refreshComboBoxData();
            } else if (selectedIndex == 1) {
                // If switching onto the "Return" view index
                returnPanel.refreshComboBoxData();
            }
        });

        this.add(tabbedPane);
    }

    /**
     * Forces JTabbedPane headers to stretch across the full width evenly.
     */
    private void configureEqualWidthTabs(JTabbedPane tabbedPane) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            final int index = i;
            String title = tabbedPane.getTitleAt(index);

            // GridBagLayout panel causes components to expand evenly
            JPanel tabComponent = new JPanel(new GridBagLayout());
            tabComponent.setOpaque(false);

            JLabel label = new JLabel(title, SwingConstants.CENTER);
            label.setFont(tabbedPane.getFont());
            label.setForeground(new Color(44, 62, 80));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            tabComponent.add(label, gbc);

            // Assign custom rendering panel over standard native titles
            tabbedPane.setTabComponentAt(index, tabComponent);
        }

        // Changes UI design configurations so tabs map uniformly across available
        // window frame space
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                // Returns an even distribution sizing width calculation based on full view
                // dimensions
                return tabbedPane.getWidth() / tabbedPane.getTabCount() - 3;
            }
        });
    }

    /**
     * Requirement: Interactive Circulation Engine with comprehensive error
     * validation handling
     * NOTE: Since you moved this functional logic to your subpanels, this method
     * can be removed or kept as a legacy helper.
     */
    private void executeCirculationPipeline(String chosenAction) {
        // ... (Legacy code left untouched for compiler safety)
    }

    private void clearInputs() {
        if (txtUserId != null)
            txtUserId.setText("");
        if (txtItemId != null)
            txtItemId.setText("");
    }
}
