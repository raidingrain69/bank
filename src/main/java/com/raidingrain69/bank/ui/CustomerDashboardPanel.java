package com.raidingrain69.bank.ui;

import com.raidingrain69.bank.controller.BankController;
import com.raidingrain69.bank.domain.Account;
import com.raidingrain69.bank.domain.Transaction;
import com.raidingrain69.bank.domain.User;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.util.List;

public final class CustomerDashboardPanel extends JPanel {
    private final BankController controller;
    private final Runnable onLogout;
    private final JComboBox<Account> accountCombo = new JComboBox<>();
    private final JLabel balanceLabel = new JLabel("Balance: -");
    private final JTextArea historyArea = new JTextArea(16, 60);
    private final JTextField targetField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JCheckBox externalTransfer = new JCheckBox("External transfer");
    private User currentUser;

    public CustomerDashboardPanel(BankController controller, Runnable onLogout) {
        this.controller = controller;
        this.onLogout = onLogout;

        setLayout(new BorderLayout(12, 12));
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        accountCombo.setRenderer(new AccountListRenderer());

        JPanel top = new JPanel();
        top.setBackground(Theme.PANEL);
        top.add(new JLabel("My Accounts"));
        top.add(accountCombo);
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");
        top.add(refreshButton);
        top.add(logoutButton);
        top.add(balanceLabel);

        historyArea.setEditable(false);
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Theme.PANEL);
        center.add(new JLabel("Transaction History"), BorderLayout.NORTH);
        center.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JPanel transfer = new JPanel();
        transfer.setLayout(new BoxLayout(transfer, BoxLayout.Y_AXIS));
        transfer.setBorder(BorderFactory.createTitledBorder("Funds Transfer"));
        transfer.add(new JLabel("Target Account/Reference"));
        transfer.add(targetField);
        transfer.add(Box.createVerticalStrut(6));
        transfer.add(new JLabel("Amount"));
        transfer.add(amountField);
        transfer.add(externalTransfer);
        JButton transferButton = new JButton("Send Transfer");
        transfer.add(transferButton);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(transfer, BorderLayout.EAST);

        accountCombo.addActionListener(event -> refreshAccountView());
        refreshButton.addActionListener(event -> refreshAccountView());
        logoutButton.addActionListener(event -> onLogout.run());
        transferButton.addActionListener(event -> doTransfer());
    }

    public void load(User user) {
        this.currentUser = user;
        accountCombo.removeAllItems();
        List<Account> accounts = controller.userAccounts(user.getId());
        accounts.forEach(accountCombo::addItem);
        refreshAccountView();
    }

    private void refreshAccountView() {
        Account selected = (Account) accountCombo.getSelectedItem();
        if (selected == null) {
            balanceLabel.setText("Balance: -");
            historyArea.setText("");
            return;
        }
        balanceLabel.setText("Balance: " + controller.accountBalance(selected.getId()));
        StringBuilder historyText = new StringBuilder();
        for (Transaction transaction : controller.accountHistory(selected.getId())) {
            historyText.append(transaction.getCreatedAt())
                    .append(" | ")
                    .append(transaction.getType())
                    .append(" | ")
                    .append(transaction.getAmount())
                    .append(" | ")
                    .append(transaction.getDescription())
                    .append(System.lineSeparator());
        }
        historyArea.setText(historyText.toString());
    }

    private void doTransfer() {
        try {
            Account source = (Account) accountCombo.getSelectedItem();
            if (source == null || currentUser == null) {
                return;
            }
            String target = targetField.getText().trim();
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            if (externalTransfer.isSelected()) {
                controller.transferExternal(source.getId(), target, amount);
            } else {
                controller.transferInternal(source.getId(), target, amount);
            }
            refreshAccountView();
            JOptionPane.showMessageDialog(this, "Transfer successful");
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transfer failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
