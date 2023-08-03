package br.com.exercise.apam.controller;

import java.util.*;

import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.Transaction;
import br.com.exercise.apam.model.TransactionParty;

/**
 * Controls all of Apam and Their Child Accounts
 */
public final class AccountController {

    private final Map<String, Account> accounts;

    /**
     * Builds a new AccountController with an empty account list
     */
    public AccountController() {
        this.accounts = new HashMap<>();

    }

    /**
     * Returns the Current Accounts registered
     *
     * @return List of Registered Accounts
     */
    public List<Account> getAccounts() {

        return new ArrayList<>(accounts.values());
    }

    /**
     * Returns an Account
     *
     * @param transactionParty TransactionParty that owns the account
     *
     * @return Account object
     *
     * @throws NullPointerException if any of the parameters are null
     */
    public Account getAccount(final TransactionParty transactionParty) {
        if (transactionParty == null) {
            throw new NullPointerException("TransactionParty cannot be null");
        }
        String transactionPartyId = transactionParty.getId();
        return getAccount(transactionPartyId);
    }

    /**
     * Returns an Account by it's transactionPartyId
     *
     * @param transactionPartyId Id of the TransactionParty that owns the account
     *
     * @return Account object or null if the account is not found
     *
     * @throws NullPointerException if any of the parameters are null
     */
    public Account getAccount(final String transactionPartyId) {
        if (transactionPartyId == null) {
            throw new NullPointerException("TransactionParty cannot be null");
        }
        return accounts.get(transactionPartyId);
    }

    /**
     * Add a new Account for a TransactionParty following these rules:
     * 1. If an account already exists with the same id, it should not be added
     *
     * @param transactionParty TransactionParty that owns the account
     * @param account Account to be added
     *
     * @throws NullPointerException if any of the parameters are null
     */
    public void addAccount(final TransactionParty transactionParty, final Account account) {
        Objects.requireNonNull(transactionParty, "TransactionParty cannot be null");
        Objects.requireNonNull(account, "Account cannot be null");

        String transactionPartyId = transactionParty.getId();
        accounts.putIfAbsent(transactionPartyId, account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountController that = (AccountController) o;
        return Objects.equals(accounts, that.accounts);
    }

    public void addTransaction(TransactionParty source, Transaction transaction) {
    }
}
