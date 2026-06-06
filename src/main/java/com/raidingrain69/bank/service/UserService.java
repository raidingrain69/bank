package com.raidingrain69.bank.service;

import com.raidingrain69.bank.auth.AuthenticationManager;
import com.raidingrain69.bank.auth.DeviceType;
import com.raidingrain69.bank.domain.Admin;
import com.raidingrain69.bank.domain.Customer;
import com.raidingrain69.bank.domain.User;
import com.raidingrain69.bank.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public User registerCustomer(String fullName, String username, String secret) {
        ensureUniqueUsername(username);
        User user = new Customer(UUID.randomUUID().toString(), fullName, username, SecurityUtils.hash(secret));
        userRepository.save(user);
        return user;
    }

    public User registerAdmin(String fullName, String username, String secret) {
        ensureUniqueUsername(username);
        User user = new Admin(UUID.randomUUID().toString(), fullName, username, SecurityUtils.hash(secret));
        userRepository.save(user);
        return user;
    }

    public Optional<User> authenticate(String username, DeviceType deviceType, String credential) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return authenticationManager.authenticate(user.get(), deviceType, credential) ? user : Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    private void ensureUniqueUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
    }
}
