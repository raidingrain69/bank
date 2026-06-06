package com.raidingrain69.bank.repository;

import com.raidingrain69.bank.domain.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accountsById = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        accountsById.put(account.getId(), account);
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accountsById.get(id));
    }

    @Override
    public List<Account> findByOwnerId(String ownerUserId) {
        return accountsById.values().stream()
                .filter(account -> account.getOwnerUserId().equals(ownerUserId))
                .toList();
    }

    @Override
    public List<Account> findAll() {
        return accountsById.values().stream().toList();
    }
}
