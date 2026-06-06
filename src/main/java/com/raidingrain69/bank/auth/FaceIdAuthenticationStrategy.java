package com.raidingrain69.bank.auth;

import com.raidingrain69.bank.domain.User;

public final class FaceIdAuthenticationStrategy implements AuthenticationStrategy {
    @Override
    public DeviceType deviceType() {
        return DeviceType.IOS;
    }

    @Override
    public boolean authenticate(User user, String credential) {
        return user.verifySecret(credential);
    }

    @Override
    public String promptLabel() {
        return "Face ID Token";
    }
}
