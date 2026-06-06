package com.raidingrain69.bank.ui;

import com.raidingrain69.bank.domain.Account;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

public final class AccountListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Account account) {
            value = account.getAccountType() + " - " + account.getId();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
