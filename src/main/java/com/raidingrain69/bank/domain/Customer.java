package com.raidingrain69.bank.domain;

public final class Customer extends User {
    public Customer(String id, String fullName, String username, String passwordHash) {
        super(id, fullName, username, passwordHash);
    }

    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }
}
