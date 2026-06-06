package com.raidingrain69.bank.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Account {
    private final String id;
    private final String ownerUserId;
    private final AccountType accountType;
    private BigDecimal balance;
    private boolean frozen;
    private final List<Transaction> transactions;

    protected Account(String id, String ownerUserId, AccountType accountType, BigDecimal openingBalance) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.accountType = accountType;
        this.balance = openingBalance;
        this.transactions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized boolean isFrozen() {
        return frozen;
    }

    public synchronized void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public synchronized List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public synchronized void deposit(BigDecimal amount, String description) {
        ensureActiveAndPositive(amount);
        balance = balance.add(amount);
        transactions.add(new Transaction(UUID.randomUUID().toString(), TransactionType.DEPOSIT, amount, description, LocalDateTime.now()));
    }

    public synchronized void withdraw(BigDecimal amount, String description) {
        ensureActiveAndPositive(amount);
        if (!canWithdraw(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(UUID.randomUUID().toString(), TransactionType.WITHDRAWAL, amount, description, LocalDateTime.now()));
    }

    public synchronized void transferOut(BigDecimal amount, String target) {
        ensureActiveAndPositive(amount);
        if (!canWithdraw(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(UUID.randomUUID().toString(), TransactionType.TRANSFER_OUT, amount, "Transfer to " + target, LocalDateTime.now()));
    }

    public synchronized void transferIn(BigDecimal amount, String source) {
        ensureActiveAndPositive(amount);
        balance = balance.add(amount);
        transactions.add(new Transaction(UUID.randomUUID().toString(), TransactionType.TRANSFER_IN, amount, "Transfer from " + source, LocalDateTime.now()));
    }

    public synchronized void externalTransfer(BigDecimal amount, String destinationReference) {
        ensureActiveAndPositive(amount);
        if (!canWithdraw(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(UUID.randomUUID().toString(), TransactionType.EXTERNAL_TRANSFER, amount,
                "External transfer to " + destinationReference, LocalDateTime.now()));
    }

    protected synchronized void ensureActiveAndPositive(BigDecimal amount) {
        if (frozen) {
            throw new IllegalStateException("Account is frozen");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    protected abstract boolean canWithdraw(BigDecimal amount);
}
