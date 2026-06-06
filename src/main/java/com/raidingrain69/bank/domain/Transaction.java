package com.raidingrain69.bank.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Transaction {
    private final String id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDateTime createdAt;

    public Transaction(String id, TransactionType type, BigDecimal amount, String description, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
