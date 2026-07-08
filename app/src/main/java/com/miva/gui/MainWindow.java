package com.miva.gui;

import com.miva.controller.LibraryManager;
import com.miva.gui.items.ItemsViewPanel;
import com.miva.gui.users.UserManagementHub;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    private LibraryManager manager;
    private CardLayout cardLayout;
    private JPanel centerContentPane;
    private JLabel lblStatus;

    // Converted Panels Cache
    private HomePanel homePanel;
    private AdminPanel adminPanel;
    private ItemsViewPanel viewItemsPanel;
    private JPanel borrowReturnPanel;
    private JPanel searchSortPanel;

    public MainWindow() {
        setTitle("Smart Library Circulation & Automation System");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        this.setLayout(new BorderLayout());

        this.manager = new LibraryManager();

        // 1. BUILD ROUTER DECKS (CardLayout)
        cardLayout = new CardLayout();
        centerContentPane = new JPanel(cardLayout);

        homePanel = new HomePanel(manager, this);
        adminPanel = new AdminPanel(manager);
        viewItemsPanel = new ItemsViewPanel(manager, this);
        borrowReturnPanel = new BorrowPanel(manager);
        searchSortPanel = createPlaceholderPanel("Search, Sort & Filters");
        UserManagementHub userHub = new UserManagementHub(manager.getUserManager());
        

        centerContentPane.add(homePanel, "Home");
        centerContentPane.add(adminPanel, "Admin");
        centerContentPane.add(viewItemsPanel, "ViewItems");
        centerContentPane.add(borrowReturnPanel, "BorrowReturn");
        centerContentPane.add(searchSortPanel, "SearchSort");
        centerContentPane.add(userHub, "UserManagement");

        // 2. ATTACH THE SEPARATE PREMIUM SIDEBAR COMPONENT
        // Lambda interceptor seamlessly catches string tokens whenever nav nodes are clicked
        SideBar navigationSidebar = new SideBar(viewKey -> switchScreen(viewKey));

        // 3. ASSEMBLE PANE REGIONS
        this.add(navigationSidebar, BorderLayout.WEST);
        this.add(centerContentPane, BorderLayout.CENTER);
        this.add(createStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    public void switchScreen(String screenName) {
        cardLayout.show(centerContentPane, screenName);
        lblStatus.setText("Viewing Active Panel: " + screenName + " Dashboard view frame.");
        
        // Refresh telemetry database metrics instances whenever navigating back Home
        if ("Home".equals(screenName)) {
            homePanel.refreshMetrics();
        }
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(236, 240, 241));
        statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(189, 195, 199)),
            new EmptyBorder(5, 10, 5, 10)
        ));
        lblStatus = new JLabel("System Operational | Connected to JSON File Engine Cache");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(127, 140, 141));
        statusBar.add(lblStatus, BorderLayout.WEST);
        return statusBar;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(149, 165, 166));
        panel.add(label);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
