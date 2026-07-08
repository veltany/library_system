package com.miva.gui.items;

import com.miva.controller.LibraryManager;
import com.miva.gui.MainWindow;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ItemsViewPanel extends JPanel {

    // categories panel
    private final BooksPanel booksPanel;
    private final MagazinesPanel magazinesPanel;
    private final JournalsPanel journalsPanel;
   

    public ItemsViewPanel(LibraryManager manager, MainWindow mainFrame) {
        this.setLayout(new BorderLayout(0, 15));
        this.setBorder(new EmptyBorder(25, 25, 25, 25));
        this.setBackground(new Color(223, 224, 228)); // Matches your exact background code tint #dfe0e4

        // 1. TOP BAR: Horizontal Header Flow row structure
        JPanel topHeaderRow = new JPanel(new BorderLayout());
        topHeaderRow.setBackground(null);

        JPanel textGroup = new JPanel(new GridLayout(2, 1, 2, 2));
        textGroup.setBackground(null);
        JLabel lblHeader = new JLabel("Library Catalogue View");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(new Color(44, 62, 80));
        
        JLabel lblSub = new JLabel("Browse, filter, and track all current literature stock items here.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(127, 140, 141));
        textGroup.add(lblHeader);
        textGroup.add(lblSub);

        // Action Trigger Buttons
        JButton btnLaunchPopup = setActionButtons(manager);
        



         // Instantiate display sub-panels
        this.booksPanel = new BooksPanel(manager);
        this.magazinesPanel = new MagazinesPanel(manager); 
        this.journalsPanel = new JournalsPanel(manager);  

   

        topHeaderRow.add(textGroup, BorderLayout.WEST);
        topHeaderRow.add(btnLaunchPopup, BorderLayout.EAST);
        this.add(topHeaderRow, BorderLayout.NORTH);

        // 3. REQUIREMENT CHECK: Tabbed Panel container building subcategories seamlessly
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        
        // Inject Tab configurations
        tabbedPane.addTab("📘 Books Catalogue", booksPanel);
        tabbedPane.addTab("📰 Magazines", magazinesPanel);
        tabbedPane.addTab("🔬 Journals", journalsPanel);

        // Dynamic State synchronization change listeners updating datasets when tabs flip
        tabbedPane.addChangeListener(e -> {
            int currentTab = tabbedPane.getSelectedIndex();
            if (currentTab == 0) {
                booksPanel.refreshData();
            }
        });

        this.add(tabbedPane, BorderLayout.CENTER);
    }

   

    /*

        Setup action buttons
    */

  private JButton setActionButtons(LibraryManager manager){


    JButton btnLaunchPopup = new JButton("➕ Add New Item");
        btnLaunchPopup.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLaunchPopup.setBackground(new Color(63, 68, 87)); // Deep steel grey code #3f4457
        btnLaunchPopup.setForeground(Color.WHITE);
        btnLaunchPopup.setFocusPainted(false);
        btnLaunchPopup.setBorderPainted(false);
        btnLaunchPopup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLaunchPopup.setMargin(new Insets(8, 16, 8, 16));

     btnLaunchPopup.addActionListener(e -> {
               // 1. Fetch the master root window Frame hook using Swing context references
    Window parentComponent = SwingUtilities.getWindowAncestor(btnLaunchPopup);
    Frame mainFrameWindow = (parentComponent instanceof Frame) ? (Frame) parentComponent : null;
    
    // 2. Launch your custom converted dialog safely on the EDT
    AddItemDialog popDialog = new AddItemDialog(mainFrameWindow, manager);
    popDialog.setVisible(true); // Blocks background execution threads until dismissed
    
    // 3. Instantly refresh data grids row matrices upon pop up dismissal
    booksPanel.refreshData();
    if (magazinesPanel instanceof MagazinesPanel) {
        ((MagazinesPanel) magazinesPanel).refreshData();
    }
    if (journalsPanel instanceof JournalsPanel) {
        ((JournalsPanel) journalsPanel).refreshData();
    }

        });


        return btnLaunchPopup;
  }
}
