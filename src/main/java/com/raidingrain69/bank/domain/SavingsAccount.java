package com.raidingrain69.bank.domain;

import java.math.BigDecimal;

public final class SavingsAccount extends Account {
    private static final BigDecimal MIN_BALANCE = new BigDecimal("100");

    public SavingsAccount(String id, String ownerUserId, BigDecimal openingBalance) {
        super(id, ownerUserId, AccountType.SAVINGS, openingBalance);
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {
        return getBalance().subtract(amount).compareTo(MIN_BALANCE) >= 0;
    }
}
