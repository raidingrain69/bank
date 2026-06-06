package com.raidingrain69.bank.ui;

import com.raidingrain69.bank.controller.BankController;
import com.raidingrain69.bank.domain.User;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public final class AdminDashboardPanel extends JPanel {
    private final BankController controller;
    private final Runnable onLogout;
    private final JLabel liquidityLabel = new JLabel();
    private final JTextArea userArea = new JTextArea(18, 70);
    private final JTextField accountIdField = new JTextField(20);

    public AdminDashboardPanel(BankController controller, Runnable onLogout) {
        this.controller = controller;
        this.onLogout = onLogout;

        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel top = new JPanel();
        top.add(new JLabel("Admin Dashboard"));
        JButton refresh = new JButton("Refresh");
        JButton logout = new JButton("Logout");
        top.add(refresh);
        top.add(logout);
        top.add(liquidityLabel);

        userArea.setEditable(false);

        JPanel actions = new JPanel();
        actions.setBorder(BorderFactory.createTitledBorder("Account Controls"));
        actions.add(new JLabel("Account ID"));
        actions.add(accountIdField);
        JButton freeze = new JButton("Freeze");
        JButton unfreeze = new JButton("Unfreeze");
        actions.add(freeze);
        actions.add(unfreeze);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(userArea), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(event -> refreshView());
        logout.addActionListener(event -> onLogout.run());
        freeze.addActionListener(event -> updateFreeze(true));
        unfreeze.addActionListener(event -> updateFreeze(false));
    }

    public void load(User adminUser) {
        refreshView();
    }

    private void refreshView() {
        liquidityLabel.setText("Total Liquidity: " + controller.totalLiquidity());
        StringBuilder users = new StringBuilder();
        controller.allUsers().forEach(user -> users
                .append(user.getRole())
                .append(" | ")
                .append(user.getFullName())
                .append(" (@")
                .append(user.getUsername())
                .append(")")
                .append(System.lineSeparator()));
        userArea.setText(users.toString());
    }

    private void updateFreeze(boolean freeze) {
        try {
            controller.freezeAccount(accountIdField.getText().trim(), freeze);
            refreshView();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Admin action", JOptionPane.ERROR_MESSAGE);
        }
    }
}
