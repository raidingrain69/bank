package com.raidingrain69.bank.service;

import com.raidingrain69.bank.domain.Account;
import com.raidingrain69.bank.domain.User;
import com.raidingrain69.bank.repository.AccountRepository;
import com.raidingrain69.bank.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

public final class AdminService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AdminService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<User> users() {
        return userRepository.findAll();
    }

    public BigDecimal totalLiquidity() {
        return accountRepository.findAll().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void freezeAccount(String accountId, boolean freeze) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        account.setFrozen(freeze);
        accountRepository.save(account);
    }
}
