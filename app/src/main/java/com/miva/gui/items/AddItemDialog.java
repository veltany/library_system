package com.miva.gui.items;

import com.miva.controller.LibraryManager;
import com.miva.model.Book;
import com.miva.model.Magazine;
import com.miva.model.Journal;
import com.miva.utils.IDGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddItemDialog extends JDialog {
    private final LibraryManager manager;
    
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtYear;
    private JTextField txtSpec;
    private JComboBox<String> comboType;
    private JLabel lblSpec;

    public AddItemDialog(Frame owner, LibraryManager manager) {
        super(owner, "Add New Inventory Item", true); // True forces APPLICATION_MODAL freezing mechanics
        this.manager = manager;
        
        // 1. Configure Layout Containers
        this.setSize(500, 380);
        this.setLocationRelativeTo(owner); // Spawns perfectly centered on top of the parent window
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel rootPane = new JPanel(new BorderLayout(0, 15));
        rootPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        rootPane.setBackground(Color.WHITE);

        // 2. FORM ENGINE: Requirement Checklist GridBagLayout
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Classification Category
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formGrid.add(new JLabel("Item Type:"), gbc);
        comboType = new JComboBox<>(new String[]{"Book", "Magazine", "Journal"});
        comboType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formGrid.add(comboType, gbc);

        // Row 1: Document Title text field
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formGrid.add(new JLabel("Title:"), gbc);
        txtTitle = new JTextField();
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formGrid.add(txtTitle, gbc);

        // Row 2: Author / Publisher Field
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formGrid.add(new JLabel("Author / Publisher:"), gbc);
        txtAuthor = new JTextField();
        txtAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formGrid.add(txtAuthor, gbc);

        // Row 3: Release Timeline Tracker
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formGrid.add(new JLabel("Publication Year:"), gbc);
        txtYear = new JTextField();
        txtYear.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formGrid.add(txtYear, gbc);

        // Row 4: Advanced GUI Rule: Dynamic components changing text labels at runtime
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        lblSpec = new JLabel("ISBN:");
        formGrid.add(lblSpec, gbc);
        txtSpec = new JTextField();
        txtSpec.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 0.7;
        formGrid.add(txtSpec, gbc);

        // Bind selector hook updating display strings dynamically
        comboType.addActionListener(e -> {
            String selected = (String) comboType.getSelectedItem();
            if ("Book".equals(selected)) {
                lblSpec.setText("ISBN:");
            } else if ("Magazine".equals(selected)) {
                lblSpec.setText("Issue Number:");
            } else if ("Journal".equals(selected)) {
                lblSpec.setText("Volume:");
            }
        });

        // 3. ACTIONS PANEL: Setup requested custom buttons rows
        JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonBox.setBackground(null);

        JButton btnSaveClose = new JButton("Save & Close");
        JButton btnSaveAgain = new JButton("Save & Add Again");
        JButton btnCancel = new JButton("Cancel");

        // Styling accents matching requested palette states
        btnSaveClose.setBackground(new Color(46, 204, 113));
        btnSaveClose.setForeground(Color.WHITE);
        btnSaveAgain.setBackground(new Color(52, 152, 219));
        btnSaveAgain.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(149, 165, 166));
        btnCancel.setForeground(Color.WHITE);

        // Button Click Interceptors
        btnSaveClose.addActionListener(e -> {
            if (processFormSubmission()) {
                this.dispose(); // Unload and dismiss pop up dialog framework
            }
        });

        btnSaveAgain.addActionListener(e -> {
            if (processFormSubmission()) {
                clearFormFields(); // Reset inputs for subsequent records entry
            }
        });

        btnCancel.addActionListener(e -> this.dispose());

        buttonBox.add(btnSaveClose);
        buttonBox.add(btnSaveAgain);
        buttonBox.add(btnCancel);

        // Assemble pieces into root panel frame
        rootPane.add(formGrid, BorderLayout.CENTER);
        rootPane.add(buttonBox, BorderLayout.SOUTH);
        this.add(rootPane);
    }

    /**
     * Requirement Checklist: Interactive try-catch validation blocks parsing inputs safely
     */
    private boolean processFormSubmission() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String yearStr = txtYear.getText().trim();
        String specStr = txtSpec.getText().trim();
        String selection = (String) comboType.getSelectedItem();

        // Safe Validation Check 1: Empty text fields detection
        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || specStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All metadata form fields must be fully populated.", "Validation Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            // Safe Validation Check 2: Correct integer casting check
            int year = Integer.parseInt(yearStr);
            String uniqueID = IDGenerator.generateUniqueID();

            // Direct model catalog persistence injection pipelines
            switch (selection) {
                case "Book" -> manager.addItem(new Book(uniqueID, title, author, year, specStr));
                case "Magazine" -> {
                    int issueNum = Integer.parseInt(specStr);
                    manager.addItem(new Magazine(uniqueID, title, author, year, issueNum));
                }
                case "Journal" -> manager.addItem(new Journal(uniqueID, title, author, year, specStr));
            }

            JOptionPane.showMessageDialog(this, selection + " added into catalog file storage.\nAssigned System ID: " + uniqueID, "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;

        } catch (NumberFormatException ex) {
            // Fault management tracking bad number casting strings
            String prompt = "Data conversion layout fault:\n";
            if ("Magazine".equals(selection)) {
                prompt += "'Publication Year' and 'Issue Number' parameters require numeric digits.";
            } else {
                prompt += "'Publication Year' field requires an integer value.";
            }
            JOptionPane.showMessageDialog(this, prompt, "Data Entry Type Conflict", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void clearFormFields() {
        txtTitle.setText("");
        txtAuthor.setText("");
        txtYear.setText("");
        txtSpec.setText("");
        comboType.setSelectedIndex(0);
        txtTitle.requestFocus(); // Move caret pointer back to top input box automatically
    }
}
