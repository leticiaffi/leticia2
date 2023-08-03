package br.com.exercise.apam.model;

/**
 * This Enum represent the subject of financial transaction that can be created
 */
public enum TransactionSubject {
    /**
     * Donation subject is a donation made by someone (anonymous or not).
     * This transaction is of type IN and allows anonymous
     */
    DONATION(TransactionType.IN, true),

    /**
     * Payment Made to an Employee or Company to pay expenses
     * This transaction is of type OUT and does not allow anonymous
     */
    PAYMENT(TransactionType.OUT),

    /**
     * Child monthly payment made by City Hall
     * This transaction is of type IN and does not allow anonymous
     */
    CHILD_PAYMENT(TransactionType.IN),

    /**
     * Child salary received by working
     * This transaction is of type IN and does not allow anonymous
     */
    CHILD_SALARY(TransactionType.IN);

    private final boolean anonymousAllowed;
    private final TransactionType transactionType;

    /**
     * Builds a new FinancialTransactionType setting the default value for anonymousAllowed as False.
     *
     * @param transactionType TransactionType of the subject
     */
    TransactionSubject(final TransactionType transactionType) {
        this(transactionType, false);
    }

    /**
     * Builds a new FinancialTransactionType.
     *
     * @param transactionType TransactionType of the subject
     * @param anonymousAllowed True if anonymous payments are allowed or False if not.
     */
    TransactionSubject(TransactionType transactionType, boolean anonymousAllowed) {
        this.transactionType = transactionType;
        this.anonymousAllowed = anonymousAllowed;
    }

    /**
     * Return if this transaction allows anonymous transactions.
     *
     * @return True if allows or False it not (default).
     */
    public boolean isAnonymousAllowed() {
        return this.anonymousAllowed;
    }

    /**
     * Returns the transaction type related to this subject
     *
     * @return TransactionType of the subject
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }
}
