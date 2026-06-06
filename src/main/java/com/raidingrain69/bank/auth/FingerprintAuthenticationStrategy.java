package com.raidingrain69.bank.auth;

import com.raidingrain69.bank.domain.User;

public final class FingerprintAuthenticationStrategy implements AuthenticationStrategy {
    @Override
    public DeviceType deviceType() {
        return DeviceType.ANDROID;
    }

    @Override
    public boolean authenticate(User user, String credential) {
        return user.verifySecret(credential);
    }

    @Override
    public String promptLabel() {
        return "Fingerprint Token";
    }
}
