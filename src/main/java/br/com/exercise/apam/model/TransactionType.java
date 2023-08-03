package br.com.exercise.apam.model;

/**
 * This enum represents the type of the transaction
 */
public enum TransactionType {
    /**
     * IN transactions bring something to Apam.
     */
    IN,

    CHILD_PAYMENT,
    PAYMENT,
    CHILD_SALARY,
    DONATION,
    /**
     * Out transactions take something from Apam.
     */
    OUT
}
