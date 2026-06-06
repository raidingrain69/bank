package com.raidingrain69.bank.domain;

public final class Admin extends User {
    public Admin(String id, String fullName, String username, String passwordHash) {
        super(id, fullName, username, passwordHash);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }
}
