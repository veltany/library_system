package com.miva.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class SideBar extends JPanel {
    private final Consumer<String> onViewChange;
    private final Color activeBg = new Color(56, 189, 248);   // Premium Sky Blue (#38bdf8)
    private final Color activeFg = new Color(15, 23, 42);     // Slate Dark Slate (#0f172a)
    private final Color idleBg = new Color(30, 41, 59);       // Slate Sidebar base (#1e293b)
    private final Color idleFg = new Color(148, 163, 184);   // Slate Light Muted (#94a3b8)
    private final Color hoverBg = new Color(51, 65, 85);      // Slate Accent Hover (#334155)

    public SideBar(Consumer<String> onViewChange) {
        this.onViewChange = onViewChange;

        // 1. Structural Configuration
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(24, 16, 24, 16));
        this.setBackground(idleBg);
        
        // Lock width dimensions matching your previous layout metrics boundary rules
        this.setPreferredSize(new Dimension(240, 0));
        this.setMinimumSize(new Dimension(200, 0));
        this.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));

        // 2. Application Brand / Header Logo Section
        JPanel brandBox = new JPanel();
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.setBackground(null);
        brandBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoIcon = new Label("📚").getParent() != null ? new JLabel() : new JLabel("📚");
        logoIcon.setText("📚");
        logoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandTitle = new JLabel("MivaLibrary");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandTitle.setForeground(Color.WHITE);
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brandSubtitle = new JLabel("Automation System");
        brandSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        brandSubtitle.setForeground(new Color(148, 163, 184));
        brandSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandBox.add(logoIcon);
        brandBox.add(Box.createVerticalStrut(4));
        brandBox.add(brandTitle);
        brandBox.add(Box.createVerticalStrut(4));
        brandBox.add(brandSubtitle);
        
        this.add(brandBox);
        this.add(Box.createVerticalStrut(30)); // Spacer beneath branding header

        // 3. Navigation Actions Components Generation
        JButton btnHome = createNavigationButton("🏠  Dashboard", false);
        JButton btnAdmin = createNavigationButton("⚙️  Admin Panel", false);
        JButton btnView = createNavigationButton("📋  View Items", false);
        JButton btnBorrow = createNavigationButton("🔄  Borrow / Return", false);
        JButton btnSearch = createNavigationButton("🔍  Search & Sort", false);

        JButton[] navButtons = {btnHome, btnAdmin, btnView, btnBorrow, btnSearch};
        
        // Start out with Dashboard locked into focus state
        applyButtonState(btnHome, true);

        btnHome.addActionListener(e -> handleNavClick("Home", btnHome, navButtons));
        btnAdmin.addActionListener(e -> handleNavClick("Admin", btnAdmin, navButtons));
        btnView.addActionListener(e -> handleNavClick("ViewItems", btnView, navButtons));
        btnBorrow.addActionListener(e -> handleNavClick("BorrowReturn", btnBorrow, navButtons));
        btnSearch.addActionListener(e -> handleNavClick("SearchSort", btnSearch, navButtons));

        this.add(btnHome);   this.add(Box.createVerticalStrut(8));
        this.add(btnAdmin);  this.add(Box.createVerticalStrut(8));
        this.add(btnView);   this.add(Box.createVerticalStrut(8));
        this.add(btnBorrow); this.add(Box.createVerticalStrut(8));
        this.add(btnSearch);

        // 4. Flexible Layout Spacer pushing footer down to the edge bottom margin
        this.add(Box.createVerticalGlue());

        // 5. System Environment Context Footer Panel
        JLabel lblFooter = new JLabel("v1.0.0 Stable Pro");
        lblFooter.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblFooter.setForeground(new Color(100, 116, 139));
        lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblFooter);
    }

    private JButton createNavigationButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Responsive full width stretch behavior
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(12, 16, 12, 16));

        applyButtonState(btn, isActive);

        // Dynamic mouse interaction styles matching old CSS properties configurations
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(activeBg)) {
                    btn.setBackground(hoverBg);
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(activeBg)) {
                    applyButtonState(btn, false);
                }
            }
        });

        return btn;
    }

    private void handleNavClick(String viewKey, JButton targetedBtn, JButton[] allButtons) {
        // Reset all buttons to basic inactive idle colors
        for (JButton btn : allButtons) {
            applyButtonState(btn, false);
        }
        // Lock selected button into active focus accent highlight
        applyButtonState(targetedBtn, true);

        // Pass selection back to MainWindow routing switch layer
        onViewChange.accept(viewKey);
    }

    private void applyButtonState(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(activeBg);
            btn.setForeground(activeFg);
        } else {
            btn.setBackground(idleBg);
            btn.setForeground(idleFg);
        }
    }
}
