package com.miva.gui.users;

import com.miva.controller.UserManager;
import javax.swing.*;
import java.awt.*;

public class UserManagementHub extends JPanel {
    private CardLayout cardLayout;
    private UsersPanel usersPanel;
    private SingleUserPanel singleUserPanel;

    public UserManagementHub(UserManager userManager) {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        usersPanel = new UsersPanel(userManager, this);
        singleUserPanel = new SingleUserPanel(userManager, this);

        this.add(usersPanel, "ListCard");
        this.add(singleUserPanel, "ProfileCard");

        showListCard();
    }

    public void showListCard() {
        usersPanel.refreshUserGrid();
        cardLayout.show(this, "ListCard");
    }

    public void openProfileView(String userId) {
        singleUserPanel.loadProfileDetails(userId);
        cardLayout.show(this, "ProfileCard");
    }
}
