package br.com.exercise.apam.controller;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.exercise.apam.dataprovider.AccountDataProvider;
import br.com.exercise.apam.dataprovider.TransactionPartyDataProvider;
import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;
import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.AccountType;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;

@CamelCaseDisplayNameGenerator
class AccountControllerTest {

    private static final TransactionParty CHILD = TransactionPartyDataProvider.provide(TransactionPartyType.CHILD);
    private static final Account CHILD_ACCOUNT = AccountDataProvider.provide(AccountType.CHILD_ACCOUNT);

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        this.accountController = new AccountController();
    }

    @Test
    void shouldBuildControllerWithEmptyAccountMap() {
        assertNotNull(accountController.getAccounts());
        assertTrue(accountController.getAccounts().isEmpty());
    }

    @Test
    void shouldReturnAccounts() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertNotNull(accountController.getAccounts());
        assertEquals(1, accountController.getAccounts().size());
        assertEquals(CHILD_ACCOUNT, accountController.getAccounts().get(0));
    }

    @Test
    void shouldReturnAccountForTransactionParty() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertEquals(CHILD_ACCOUNT, accountController.getAccount(CHILD));
    }

    @Test
    void shouldReturnNullAccountWhenTransactionPartyDoesNotHaveOne() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertNull(accountController.getAccount(TransactionPartyDataProvider.provide(TransactionPartyType.DONOR)));
    }

    @Test
    void shouldReturnAccountForTransactionPartyId() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertEquals(CHILD_ACCOUNT, accountController.getAccount(CHILD.getId()));
    }

    @Test
    void shouldReturnNullAccountWhenTransactionPartyIdDoesNotHaveOne() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertNull(accountController.getAccount(provideId()));
    }

    @Test
    void shouldAddAccount() {
        accountController.addAccount(CHILD, CHILD_ACCOUNT);

        assertNotNull(accountController.getAccounts());
        assertEquals(1, accountController.getAccounts().size());
        assertEquals(CHILD_ACCOUNT, accountController.getAccounts().get(0));
    }

    @Test
    void shouldNotAddDuplicatedAccount() {
        final Account account = AccountDataProvider.provide(AccountType.CHILD_ACCOUNT);
        accountController.addAccount(CHILD, CHILD_ACCOUNT);
        accountController.addAccount(CHILD, account);

        assertNotNull(accountController.getAccounts());
        assertEquals(1, accountController.getAccounts().size());
        assertEquals(CHILD_ACCOUNT, accountController.getAccounts().get(0));
    }
}