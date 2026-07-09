package com.miva.gui.items;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class SortControlPanel extends JPanel {

    private final JComboBox<String> criteriaDropdown;
    private final JToggleButton orderToggle;
    private final JButton btnSort;

    // Functional interface to trigger sorting on the data source
    public interface SortAction {
        void execute(String criteria, boolean isAscending);
    }

    public SortControlPanel(String[] criteriaOptions, SortAction sortAction) {
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        this.setBackground(Color.WHITE);

        // Criteria Selection (e.g., Title, Author, Year)
        this.add(new JLabel("Sort By:"));
        criteriaDropdown = new JComboBox<>(criteriaOptions);
        criteriaDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        this.add(criteriaDropdown);

        // Direction Toggle Button
        orderToggle = new JToggleButton("▲ Ascending");
        orderToggle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        orderToggle.setFocusPainted(false);
        orderToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderToggle.addActionListener(e -> {
            if (orderToggle.isSelected()) {
                orderToggle.setText("▼ Descending");
            } else {
                orderToggle.setText("▲ Ascending");
            }
        });
        this.add(orderToggle);

        // 4. Execution Button
        btnSort = new JButton("Sort Now");
        btnSort.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSort.setBackground(new Color(41, 128, 185)); // Nice Ocean Blue
        btnSort.setForeground(Color.WHITE);
        btnSort.setFocusPainted(false);
        btnSort.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Execute the callback whenever clicked
        btnSort.addActionListener(e -> {

            String selectedCriteria = (String) criteriaDropdown.getSelectedItem();
            boolean isAscending = !orderToggle.isSelected(); // Unselected means Ascending
            sortAction.execute(selectedCriteria, isAscending);
        });
        this.add(btnSort);
    }
}
