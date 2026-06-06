package com.raidingrain69.bank.auth;

import com.raidingrain69.bank.domain.User;

public interface AuthenticationStrategy {
    DeviceType deviceType();

    boolean authenticate(User user, String credential);

    String promptLabel();
}
