package com.raidingrain69.bank.repository;

import com.raidingrain69.bank.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);

    Optional<Account> findById(String id);

    List<Account> findByOwnerId(String ownerUserId);

    List<Account> findAll();
}
