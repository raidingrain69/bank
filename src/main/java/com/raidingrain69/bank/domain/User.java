package com.raidingrain69.bank.domain;

import com.raidingrain69.bank.service.SecurityUtils;

public abstract class User {
    private final String id;
    private final String fullName;
    private final String username;
    private final String passwordHash;

    protected User(String id, String fullName, String username, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public boolean verifySecret(String rawSecret) {
        return SecurityUtils.matches(rawSecret, passwordHash);
    }

    public abstract UserRole getRole();
}
