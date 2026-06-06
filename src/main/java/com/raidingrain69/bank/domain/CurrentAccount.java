package com.raidingrain69.bank.domain;

import java.math.BigDecimal;

public final class CurrentAccount extends Account {
    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("500");

    public CurrentAccount(String id, String ownerUserId, BigDecimal openingBalance) {
        super(id, ownerUserId, AccountType.CURRENT, openingBalance);
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {
        return getBalance().add(OVERDRAFT_LIMIT).subtract(amount).signum() >= 0;
    }
}
