package com.miva.gui.items;

import com.miva.controller.LibraryManager;
import com.miva.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class BooksPanel extends JPanel {
    private final LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;

    public BooksPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout(0, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setBackground(Color.WHITE);

        // 1. Column Schemas Config
        String[] columns = {"Item ID", "Book Title", "Author / Publisher", "Year", "Availability Status"};
        
        // Ensure table grids are read-only to satisfy strict data management rules
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

        // 2. REQUIREMENT 9.1: Custom Cell Renderer to handle dynamic text decoration paint
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int r, int c) {
                Component cell = super.getTableCellRendererComponent(t, val, isSel, hasFoc, r, c);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                
                if (val != null) {
                    String status = val.toString();
                    if ("Available".equals(status)) {
                        cell.setForeground(new Color(46, 204, 113)); // Modern Emerald Green
                    } else {
                        cell.setForeground(new Color(231, 76, 60));  // Alizarin Crimson Red
                    }
                }
                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 233, 240)));
        this.add(scrollPane, BorderLayout.CENTER);

        // 3. Action Control Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setBackground(null);

        JButton btnRefresh = new java.awt.Button("🔄 Refresh Data").getParent() != null ? new JButton() : new JButton("🔄 Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(52, 73, 94));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());
        actionRow.add(btnRefresh);
        
        this.add(actionRow, BorderLayout.SOUTH);

        // Seed initial sync
        refreshData();
    }

    public void refreshData() {
        // Flush active matrix lines
        tableModel.setRowCount(0);

        // Process polymorphic stream queries
        List<Book> books = manager.getCatalogue().stream()
                .filter(item -> "Book".equals(item.getItemType()))
                .map(item -> (Book) item)
                .collect(Collectors.toList());

        // Map objects into table columns row-by-row
        for (Book book : books) {
            Object[] rowData = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getYear(),
                book.isAvailable() ? "Available" : "Borrowed"
            };
            tableModel.addRow(rowData);
        }
    }
}
