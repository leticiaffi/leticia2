package br.com.exercise.apam.model;

/**
 * This Enum represents the types of accounts
 */
public enum AccountType {
    /**
     * Accounts that receives City Hall Payments
     */
    CITY_HALL_ACCOUNT,

    /**
     * Accounts owned by children to receive their work payments
     */
    CHILD_ACCOUNT,

    /**
     * Accounts to receive donations
     */
    DONATION_ACCOUNT
}
