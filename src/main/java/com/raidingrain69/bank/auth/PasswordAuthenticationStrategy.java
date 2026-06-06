package com.raidingrain69.bank.auth;

import com.raidingrain69.bank.domain.User;

public final class PasswordAuthenticationStrategy implements AuthenticationStrategy {
    @Override
    public DeviceType deviceType() {
        return DeviceType.DESKTOP;
    }

    @Override
    public boolean authenticate(User user, String credential) {
        return credential != null && credential.length() >= 4 && user.verifySecret(credential);
    }

    @Override
    public String promptLabel() {
        return "Password / PIN";
    }
}
