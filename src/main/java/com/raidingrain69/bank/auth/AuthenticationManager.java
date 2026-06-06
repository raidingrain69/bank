package com.raidingrain69.bank.auth;

import com.raidingrain69.bank.domain.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class AuthenticationManager {
    private final Map<DeviceType, AuthenticationStrategy> strategies;

    public AuthenticationManager() {
        List<AuthenticationStrategy> defaults = List.of(
                new FingerprintAuthenticationStrategy(),
                new FaceIdAuthenticationStrategy(),
                new PasswordAuthenticationStrategy()
        );
        this.strategies = new EnumMap<>(DeviceType.class);
        defaults.forEach(strategy -> strategies.put(strategy.deviceType(), strategy));
    }

    public boolean authenticate(User user, DeviceType deviceType, String credential) {
        AuthenticationStrategy strategy = strategies.get(deviceType);
        if (strategy == null || user == null) {
            return false;
        }
        return strategy.authenticate(user, credential);
    }

    public String promptFor(DeviceType deviceType) {
        AuthenticationStrategy strategy = strategies.get(deviceType);
        return strategy == null ? "Credential" : strategy.promptLabel();
    }
}
