package br.com.exercise.apam.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class Represents an Apam or Child Account
 */
public class Account {

    private final String id;
    private final String number;
    private final AccountType type;
    private final List<Transaction> transactions;

    /**
     * Create a new Account with the following rules:
     * 1. Id, Number and Type should not be null
     * 2. Transaction List should be an empty list
     *
     * @param id Id of the Account
     * @param number Number of the account
     * @param type AccountType of the account
     *
     * @throws NullPointerException if id or number are null
     */
    public Account(String id, String number, AccountType type) {
        if (id == null) {
            throw new NullPointerException("id must not be null");
        }
        if (number == null ) {
            throw new NullPointerException("number must not be null");
        }
        if (type == null) {
            throw new NullPointerException("type must not be null");
        }

        this.id = id;
        this.number = number;
        this.type = type;
        this.transactions = new ArrayList<>();
    }

    /**
     * Returns the Id of the Account.
     *
     * @return String representing the id of the Account.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the Number of the Account.
     *
     * @return String representing the Number of the Account.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Returns the Type of the Account.
     *
     * @return AccountType representing the Type of the Account.
     */
    public AccountType getType() {
        return type;
    }

    /**
     * Returns the Transactions of the Account.
     *
     * @return List of Transactions representing the Transactions of the Account.
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns the Balance of the Account.
     *
     * @return BigDecimal representing the Balance of the Account.
     */
    public BigDecimal getBalance() {
        BigDecimal Balance = BigDecimal.ZERO;
        if (transactions != null ) {
            for (Transaction transaction : transactions) {
                Balance = Balance.add(transaction.getAmount());
            }
        }
        return Balance;
    }

    /**
     * Add a new Transaction to the Account following these rules:
     * 1. CITY_HALL_ACCOUNT accounts only accepts [CHILD_PAYMENT, PAYMENT] transactions subject.
     * 2. CHILD_ACCOUNT accounts only accepts [CHILD_SALARY, PAYMENT] transactions subject.
     * 3. DONATION_ACCOUNT accounts only accepts [DONATION, PAYMENT] transactions subject.
     * 4. Duplicated transactions should be ignored
     *
     * @param transaction Transaction to be added
     *
     * @throws IllegalArgumentException if any of the rules above are violated
     * @throws NullPointerException if transaction is null.
     */
    public void addTransaction(final Transaction transaction) {
        if (transaction == null) {
            throw new NullPointerException(" transaction must not be null");
        }
        if (transactions.contains(transaction)) {
            return;
        }

        Set<TransactionType> allowedTransactions = new HashSet<>();
        switch (type) {
            case CITY_HALL_ACCOUNT:
                allowedTransactions.add(TransactionType.CHILD_PAYMENT);
                allowedTransactions.add(TransactionType.PAYMENT);
                break;
            case CHILD_ACCOUNT:
                allowedTransactions.add(TransactionType.CHILD_SALARY);
                allowedTransactions.add(TransactionType.PAYMENT);
                break;
            case DONATION_ACCOUNT:
                allowedTransactions.add(TransactionType.DONATION);
                allowedTransactions.add(TransactionType.PAYMENT);
                break;
        }
        if (!allowedTransactions.contains(transaction.getSubject())) {
            throw new IllegalArgumentException("This transaction is invalid, it has already been added");
        }

        transaction.add(transaction);

    }
}
