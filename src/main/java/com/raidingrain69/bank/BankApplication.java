package com.raidingrain69.bank;

import com.raidingrain69.bank.auth.AuthenticationManager;
import com.raidingrain69.bank.controller.BankController;
import com.raidingrain69.bank.repository.AccountRepository;
import com.raidingrain69.bank.repository.InMemoryAccountRepository;
import com.raidingrain69.bank.repository.InMemoryUserRepository;
import com.raidingrain69.bank.repository.UserRepository;
import com.raidingrain69.bank.service.AccountService;
import com.raidingrain69.bank.service.AdminService;
import com.raidingrain69.bank.service.UserService;
import com.raidingrain69.bank.ui.MainFrame;

import javax.swing.SwingUtilities;

public final class BankApplication {
    private BankApplication() {
    }

    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        AccountRepository accountRepository = new InMemoryAccountRepository();

        AuthenticationManager authenticationManager = new AuthenticationManager();
        UserService userService = new UserService(userRepository, authenticationManager);
        AccountService accountService = new AccountService(accountRepository);
        AdminService adminService = new AdminService(userRepository, accountRepository);

        BankController controller = new BankController(userService, accountService, adminService);
        controller.seedMockData();

        SwingUtilities.invokeLater(() -> new MainFrame(controller).setVisible(true));
    }
}
