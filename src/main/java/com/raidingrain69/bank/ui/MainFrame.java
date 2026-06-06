package com.raidingrain69.bank.ui;

import com.raidingrain69.bank.controller.BankController;
import com.raidingrain69.bank.domain.User;
import com.raidingrain69.bank.domain.UserRole;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

public final class MainFrame extends JFrame {
    private static final String LOGIN = "LOGIN";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String ADMIN = "ADMIN";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(cardLayout);
    private final LoginPanel loginPanel;
    private final CustomerDashboardPanel customerDashboardPanel;
    private final AdminDashboardPanel adminDashboardPanel;

    public MainFrame(BankController controller) {
        super("Banking Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        getContentPane().setBackground(new Color(245, 248, 252));

        this.loginPanel = new LoginPanel(controller, this::handleLogin);
        this.customerDashboardPanel = new CustomerDashboardPanel(controller, this::logout);
        this.adminDashboardPanel = new AdminDashboardPanel(controller, this::logout);

        rootPanel.add(loginPanel, LOGIN);
        rootPanel.add(customerDashboardPanel, CUSTOMER);
        rootPanel.add(adminDashboardPanel, ADMIN);

        add(rootPanel);
        cardLayout.show(rootPanel, LOGIN);
        setLocationRelativeTo(null);
    }

    private void handleLogin(User user) {
        if (user.getRole() == UserRole.ADMIN) {
            adminDashboardPanel.load(user);
            cardLayout.show(rootPanel, ADMIN);
            return;
        }
        customerDashboardPanel.load(user);
        cardLayout.show(rootPanel, CUSTOMER);
    }

    private void logout() {
        loginPanel.reset();
        cardLayout.show(rootPanel, LOGIN);
    }
}
