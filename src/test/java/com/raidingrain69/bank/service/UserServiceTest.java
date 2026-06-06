package com.raidingrain69.bank.service;

import com.raidingrain69.bank.auth.AuthenticationManager;
import com.raidingrain69.bank.auth.DeviceType;
import com.raidingrain69.bank.domain.User;
import com.raidingrain69.bank.repository.InMemoryUserRepository;
import com.raidingrain69.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    @Test
    void authenticatesAcrossDeviceStrategies() {
        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository, new AuthenticationManager());

        User user = userService.registerCustomer("Test User", "tester", "secret-123");

        Optional<User> androidLogin = userService.authenticate("tester", DeviceType.ANDROID, "secret-123");
        Optional<User> iosLogin = userService.authenticate("tester", DeviceType.IOS, "secret-123");
        Optional<User> desktopLogin = userService.authenticate("tester", DeviceType.DESKTOP, "secret-123");

        assertTrue(androidLogin.isPresent());
        assertTrue(iosLogin.isPresent());
        assertTrue(desktopLogin.isPresent());
        assertTrue(androidLogin.get().getId().equals(user.getId()));
    }
}
