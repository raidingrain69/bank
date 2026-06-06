package com.raidingrain69.bank.controller;

import com.raidingrain69.bank.auth.DeviceType;
import com.raidingrain69.bank.domain.Account;
import com.raidingrain69.bank.domain.Transaction;
import com.raidingrain69.bank.domain.User;
import com.raidingrain69.bank.service.AccountService;
import com.raidingrain69.bank.service.AdminService;
import com.raidingrain69.bank.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public final class BankController {
    private final UserService userService;
    private final AccountService accountService;
    private final AdminService adminService;

    public BankController(UserService userService, AccountService accountService, AdminService adminService) {
        this.userService = userService;
        this.accountService = accountService;
        this.adminService = adminService;
    }

    public void seedMockData() {
        User admin = userService.registerAdmin("Core Admin", "admin", "1234");
        User customerA = userService.registerCustomer("Alice Doe", "alice", "alice-pass");
        User customerB = userService.registerCustomer("Bob Doe", "bob", "bob-pass");

        Account aliceSavings = accountService.openSavingsAccount(customerA.getId(), new BigDecimal("1500"));
        accountService.openCurrentAccount(customerA.getId(), new BigDecimal("350"));
        Account bobCurrent = accountService.openCurrentAccount(customerB.getId(), new BigDecimal("600"));

        accountService.transferInternal(aliceSavings.getId(), bobCurrent.getId(), new BigDecimal("120"));

        accountService.openCurrentAccount(admin.getId(), new BigDecimal("10000"));
    }

    public Optional<User> login(String username, DeviceType deviceType, String credential) {
        return userService.authenticate(username, deviceType, credential);
    }

    public User registerCustomer(String fullName, String username, String password) {
        User user = userService.registerCustomer(fullName, username, password);
        accountService.openSavingsAccount(user.getId(), new BigDecimal("1000"));
        return user;
    }

    public List<Account> userAccounts(String userId) {
        return accountService.accountsForUser(userId);
    }

    public BigDecimal accountBalance(String accountId) {
        return accountService.balance(accountId);
    }

    public List<Transaction> accountHistory(String accountId) {
        return accountService.history(accountId);
    }

    public void transferInternal(String fromAccountId, String toAccountId, BigDecimal amount) {
        accountService.transferInternal(fromAccountId, toAccountId, amount);
    }

    public void transferExternal(String fromAccountId, String destinationReference, BigDecimal amount) {
        accountService.transferExternal(fromAccountId, destinationReference, amount);
    }

    public List<User> allUsers() {
        return adminService.users();
    }

    public BigDecimal totalLiquidity() {
        return adminService.totalLiquidity();
    }

    public void freezeAccount(String accountId, boolean freeze) {
        adminService.freezeAccount(accountId, freeze);
    }
}
