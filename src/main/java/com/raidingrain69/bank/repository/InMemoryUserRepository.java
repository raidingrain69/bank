package com.raidingrain69.bank.repository;

import com.raidingrain69.bank.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersById = new ConcurrentHashMap<>();
    private final Map<String, String> usernames = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        usersById.put(user.getId(), user);
        usernames.put(user.getUsername(), user.getId());
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String id = usernames.get(username);
        return id == null ? Optional.empty() : findById(id);
    }

    @Override
    public List<User> findAll() {
        return usersById.values().stream().toList();
    }
}
