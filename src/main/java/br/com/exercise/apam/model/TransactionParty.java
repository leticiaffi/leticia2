package br.com.exercise.apam.model;

/**
 * Represents someone that can initiate a transaction with Apam (Ex. Person, Institution).
 */
public interface TransactionParty {

    /**
     * Returns the Id of the TransactionParty.
     *
     * @return String representing the Id of the TransactionParty.
     */
    String getId();

    /**
     * Returns the Name of the TransactionParty.
     *
     * @return String representing the Name of the TransactionParty.
     */
    String getName();

    /**
     * Returns the type of the Transaction Party
     *
     * @return TransactionPartyType representing the type of the TransactionParty.
     */
    TransactionPartyType getType();

    /**
     * Returns the age of the Transaction Party
     *
     * @return Integer representing the age of the transaction party
     */
    Integer getAge();
}
