package com.miva.gui;

import com.miva.controller.LibraryManager;
import com.miva.model.Book;
import com.miva.model.Magazine;
import com.miva.model.Journal;
import com.miva.utils.IDGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminPanel extends JPanel {
    private final LibraryManager manager;
    
    // UI Form Fields
    private JTextField txtTitle, txtAuthor, txtYear, txtSpec;
    private JComboBox<String> comboType;
    private JLabel lblSpec;

    public AdminPanel(LibraryManager manager) {
        this.manager = manager;

        // Requirement Check: Top-level panel structures managed with vertical BoxLayout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(30, 30, 30, 30));
        this.setBackground(new Color(248, 249, 250)); // Soft modern white backdrop

        // 1. HEADER PANEL SECTION
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setBackground(null);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("Library Administration & Catalogue Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));

        JLabel lblSubtitle = new JLabel("Register books, academic literature, or manage system catalog mutations.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(127, 140, 141));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        this.add(headerPanel);
        this.add(Box.createVerticalStrut(20)); // Structural component margin space

        // 2. INPUT CARD: Requirement Check: Core form fields managed strictly with GridBagLayout
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 233, 240), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.setMaximumSize(new Dimension(650, 350));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Item Category Selector
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Item Classification:"), gbc);
        
        comboType = new JComboBox<>(new String[]{"Book", "Magazine", "Journal"});
        comboType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(comboType, gbc);

        // Row 1: Document Title text field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Resource Title:"), gbc);
        
        txtTitle = new JTextField(20);
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtTitle, gbc);

        // Row 2: Origin Creator Author field
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Author / Publisher:"), gbc);
        
        txtAuthor = new JTextField(20);
        txtAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtAuthor, gbc);

        // Row 3: Release Timeline input tracker
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formCard.add(createStyledLabel("Publication Year:"), gbc);
        
        txtYear = new JTextField(20);
        txtYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtYear, gbc);

        // Row 4: Advanced GUI Technique: Dynamic layout component changing strings at runtime
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        lblSpec = new JLabel("ISBN Code:");
        lblSpec.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSpec.setForeground(new Color(52, 73, 94));
        formCard.add(lblSpec, gbc);
        
        txtSpec = new JTextField(20);
        txtSpec.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formCard.add(txtSpec, gbc);

        // Add combobox action to trigger dynamic form label shifting mapping context strings
        comboType.addActionListener(e -> {
            String selected = (String) comboType.getSelectedItem();
            if ("Book".equals(selected)) {
                lblSpec.setText("ISBN Code:");
            } else if ("Magazine".equals(selected)) {
                lblSpec.setText("Issue Number:");
            } else if ("Journal".equals(selected)) {
                lblSpec.setText("Volume String:");
            }
        });

        // Row 5: Action Button Panel Integration Row Layout
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        buttonRow.setBackground(null);

        JButton btnAdd = new JButton("Add to Catalogue");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnUndo = new JButton("Undo Last Deletion");
        btnUndo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnUndo.setBackground(new Color(231, 76, 60));
        btnUndo.setForeground(Color.WHITE);
        btnUndo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonRow.add(btnAdd);
        buttonRow.add(btnUndo);

        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.7;
        formCard.add(buttonRow, gbc);

        this.add(formCard);

        // BUTTON EVENT INTERACTORS
        btnAdd.addActionListener(e -> commitFormSubmission());
        btnUndo.addActionListener(e -> {
            manager.undoLastDeletion();
            JOptionPane.showMessageDialog(this, "Last deleted registry item successfully restored into memory files.", "Recovery State", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * Requirement: Advanced Error Handling (try/catch validation blocks with dialog popups)
     */
    private void commitFormSubmission() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String yearStr = txtYear.getText().trim();
        String specStr = txtSpec.getText().trim();
        String selectedType = (String) comboType.getSelectedItem();

        // Check 1: Mandatory blank validation alert checks
        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || specStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Form Validation Failed:\nAll data item parameter values must be populated.", "Input Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check 2: Parse integer tokens checking data format safety
            int year = Integer.parseInt(yearStr);
            String uniqueID = IDGenerator.generateUniqueID();

            // Real Production Logic: Branch allocation constructing correct model subclasses
            switch (selectedType) {
                case "Book" -> {
                    Book bookObj = new Book(uniqueID, title, author, year, specStr);
                    manager.addItem(bookObj);
                }
                case "Magazine" -> {
                    // Magazines require numeric serialization checks for issue values
                    int issueNumber = Integer.parseInt(specStr);
                    Magazine magObj = new Magazine(uniqueID, title, author, year, issueNumber);
                    manager.addItem(magObj);
                }
                case "Journal" -> {
                    Journal journalObj = new Journal(uniqueID, title, author, year, specStr);
                    manager.addItem(journalObj);
                }
            }

            // Report Success via Dialog window
            JOptionPane.showMessageDialog(this, selectedType + " successfully saved!\nSystem Tracking ID: " + uniqueID, "Catalogue Mutation Confirmed", JOptionPane.INFORMATION_MESSAGE);

            // Flush out old entry texts clearing components for the next execution run
            txtTitle.setText("");
            txtAuthor.setText("");
            txtYear.setText("");
            txtSpec.setText("");
            comboType.setSelectedIndex(0);

        } catch (NumberFormatException ex) {
            // Error Handling: Catch validation conflicts without crashing system runtime lines
            String errorMessage = "Data conversion layout conflict:\n";
            if (selectedType.equals("Magazine")) {
                errorMessage += "'Publication Year' and 'Issue Number' must contain valid numeric digits.";
            } else {
                errorMessage += "'Publication Year' must contain a valid numeric integer sequence.";
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Format Type Mismatch", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
}
