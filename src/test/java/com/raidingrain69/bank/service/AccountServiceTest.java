package com.raidingrain69.bank.service;

import com.raidingrain69.bank.domain.Account;
import com.raidingrain69.bank.repository.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    @Test
    void handlesInternalTransfersAndFrozenAccounts() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        Account from = service.openCurrentAccount("u1", new BigDecimal("1000"));
        Account to = service.openSavingsAccount("u2", new BigDecimal("500"));

        service.transferInternal(from.getId(), to.getId(), new BigDecimal("100"));

        assertEquals(new BigDecimal("900"), service.balance(from.getId()));
        assertEquals(new BigDecimal("600"), service.balance(to.getId()));

        service.freezeAccount(from.getId(), true);

        assertThrows(IllegalStateException.class,
                () -> service.transferExternal(from.getId(), "EXT-REF", new BigDecimal("50")));
    }

    @Test
    void recordsSingleTransferOutTransactionForInternalTransfer() {
        AccountService service = new AccountService(new InMemoryAccountRepository());
        Account from = service.openCurrentAccount("u1", new BigDecimal("1000"));
        Account to = service.openCurrentAccount("u2", new BigDecimal("1000"));

        service.transferInternal(from.getId(), to.getId(), new BigDecimal("100"));

        assertEquals(1, service.history(from.getId()).size());
    }
}
