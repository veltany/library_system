package com.miva.gui.items;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.miva.controller.LibraryManager;
import com.miva.model.Magazine;
import com.miva.model.Refreshable;

public class MagazinesPanel extends JPanel implements Refreshable {
    private final LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public MagazinesPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout(0, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setBackground(Color.WHITE);

        // Column Schemas (Includes specific Issue Number tracker)
        String[] columns = { "ID", "Title", "Publisher", "Year", "Issue No.", "Status" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFillsViewportHeight(true);

        // Advanced GUI Technique 1.0: Custom Renderer for dynamic color coding
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int r,
                    int c) {
                Component cell = super.getTableCellRendererComponent(t, val, isSel, hasFoc, r, c);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (val != null) {
                    if ("Available".equals(val.toString())) {
                        cell.setForeground(new Color(46, 204, 113)); // Green
                    } else {
                        cell.setForeground(new Color(231, 76, 60)); // Red
                    }
                }
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 233, 240)));
        this.add(scrollPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("🔄 Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(52, 73, 94));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setBackground(null);
        actionRow.add(btnRefresh);
        this.add(actionRow, BorderLayout.SOUTH);

        refreshData();
    }

    @Override
    public void refreshData() {
        tableModel.setRowCount(0);
        List<Magazine> magazines = manager.getCatalogue().stream()
                .filter(i -> "Magazine".equals(i.getItemType()))
                .map(i -> (Magazine) i)
                .collect(Collectors.toList());

        for (Magazine m : magazines) {
            Object[] row = {
                    m.getId(),
                    m.getTitle(),
                    m.getAuthor(),
                    m.getYear(),
                    m.getIssueNumber(),
                    m.isAvailable() ? "Available" : "Borrowed"
            };
            tableModel.addRow(row);
        }
    }
}
