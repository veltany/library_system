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
import com.miva.controller.SortEngine;
import com.miva.controller.SortEngine.AlgoType;
import com.miva.model.Book;
import com.miva.model.LibraryItem;
import com.miva.model.Refreshable;

public class BooksPanel extends JPanel implements Refreshable {
    private final LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Book> cachedBooks;

    public BooksPanel(LibraryManager manager) {
        this.manager = manager;
        this.setLayout(new BorderLayout(0, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));
        this.setBackground(Color.WHITE);

        // --- NEW: Inject Reusable Sorting Component at the Top
        String[] sortCriteria = { "Author", "Title", "Year" };
        SortControlPanel sortPanel = new SortControlPanel(sortCriteria, (criteria, isAscending) -> {

            AlgoType algo = AlgoType.MERGE;
            if (cachedBooks == null || cachedBooks.isEmpty())
                return;

            // Convert list to array for SortEngine
            LibraryItem[] itemsArray = cachedBooks.toArray(new LibraryItem[0]);

            // Route sorting dynamically using SortEngine
            if ("Author".equals(criteria)) {
                SortEngine.sortByAuthor(itemsArray, algo, isAscending);
            } else if ("Title".equals(criteria)) {
                SortEngine.sortByTitle(itemsArray, algo, isAscending);
            } else if ("Year".equals(criteria)) {
                SortEngine.sortByYear(itemsArray, algo, isAscending);
            }

            cachedBooks.clear();
            for (LibraryItem item : itemsArray) {
                cachedBooks.add((Book) item);
            }

            // Refresh table view using the freshly sorted array
            updateTableData(itemsArray);
        });
        this.add(sortPanel, BorderLayout.NORTH); // Placed beautifully above table

        // 1. Column Schemas Config
        String[] columns = { "Item ID", "Book Title", "Author / Publisher", "Year", "Availability Status" };

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

        // paint
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int r,
                    int c) {
                Component cell = super.getTableCellRendererComponent(t, val, isSel, hasFoc, r, c);
                setFont(new Font("Segoe UI", Font.BOLD, 13));

                if (val != null) {
                    String status = val.toString();
                    if ("Available".equals(status)) {
                        cell.setForeground(new Color(46, 204, 113)); // Modern Emerald Green
                    } else {
                        cell.setForeground(new Color(231, 76, 60)); // Alizarin Crimson Red
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

        JButton btnRefresh = new java.awt.Button("🔄 Refresh Data").getParent() != null ? new JButton()
                : new JButton("🔄 Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(52, 73, 94));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());
        actionRow.add(btnRefresh);

        this.add(actionRow, BorderLayout.SOUTH);

        // Seed initial sync
        refreshData();
    }

    @Override
    public void refreshData() {
        // // Flush active matrix lines
        // tableModel.setRowCount(0);

        // // Process polymorphic stream queries
        // List<Book> books = manager.getCatalogue().stream()
        // .filter(item -> "Book".equals(item.getItemType()))
        // .map(item -> (Book) item)
        // .collect(Collectors.toList());

        // // Map objects into table columns row-by-row
        // for (Book book : books) {
        // Object[] rowData = {
        // book.getId(),
        // book.getTitle(),
        // book.getAuthor(),
        // book.getYear(),
        // book.isAvailable() ? "Available" : "Borrowed"
        // };
        // tableModel.addRow(rowData);
        // }

        cachedBooks = manager.getCatalogue().stream()
                .filter(item -> "Book".equals(item.getItemType()))
                .map(item -> (Book) item)
                .collect(Collectors.toList());

        updateTableData(cachedBooks.toArray(new LibraryItem[0]));
    }

    // Helper method to redraw rows safely
    private void updateTableData(LibraryItem[] items) {
        tableModel.setRowCount(0);
        for (LibraryItem item : items) {
            Book book = (Book) item;
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
