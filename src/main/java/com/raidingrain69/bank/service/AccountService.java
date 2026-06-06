package com.raidingrain69.bank.service;

import com.raidingrain69.bank.domain.Account;
import com.raidingrain69.bank.domain.CurrentAccount;
import com.raidingrain69.bank.domain.SavingsAccount;
import com.raidingrain69.bank.domain.Transaction;
import com.raidingrain69.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account openSavingsAccount(String ownerId, BigDecimal openingBalance) {
        Account account = new SavingsAccount(UUID.randomUUID().toString(), ownerId, openingBalance);
        accountRepository.save(account);
        return account;
    }

    public Account openCurrentAccount(String ownerId, BigDecimal openingBalance) {
        Account account = new CurrentAccount(UUID.randomUUID().toString(), ownerId, openingBalance);
        accountRepository.save(account);
        return account;
    }

    public List<Account> accountsForUser(String userId) {
        return accountRepository.findByOwnerId(userId);
    }

    public BigDecimal balance(String accountId) {
        return load(accountId).getBalance();
    }

    public List<Transaction> history(String accountId) {
        return load(accountId).getTransactions();
    }

    public void transferInternal(String fromAccountId, String toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Source and destination cannot match");
        }
        Account from = load(fromAccountId);
        Account to = load(toAccountId);
        from.transferOut(amount, toAccountId);
        to.transferIn(amount, fromAccountId);
        accountRepository.save(from);
        accountRepository.save(to);
    }

    public void transferExternal(String fromAccountId, String destinationReference, BigDecimal amount) {
        Account from = load(fromAccountId);
        from.externalTransfer(amount, destinationReference);
        accountRepository.save(from);
    }

    public void freezeAccount(String accountId, boolean freeze) {
        Account account = load(accountId);
        account.setFrozen(freeze);
        accountRepository.save(account);
    }

    public List<Account> allAccounts() {
        return accountRepository.findAll();
    }

    private Account load(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }
}
