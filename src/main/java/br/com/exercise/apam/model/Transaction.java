package br.com.exercise.apam.model;

import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;
import br.com.exercise.apam.model.TransactionSubject;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Transaction on Apam
 */
public class Transaction {
    private final String id;
    private final String description;
    private final BigDecimal value;
    private final TransactionSubject subject;
    private final TransactionParty party;

    /**
     * Builds a new Transaction following these rules:
     *   1. Value must always be above 0.
     *   2. DONATION subject could have only [DONOR, COMPANY] Parties.
     *   3. PAYMENT subject could have only [EMPLOYEE, SERVICE_PROVIDER, COMPANY] Parties.
     *   4. CHILD_PAYMENT subject could have only [CITY_HALL] Parties.
     *   5. CHILD_SALARY subject could have only [CHILD] Parties.
     *
     * @param id Id of the Transaction.
     * @param description Description of the Transaction.
     * @param value Value of the Transaction.
     * @param subject Subject of the Transaction.
     * @param party Party of the Transaction.
     *
     * @throws NullPointerException if any of the parameters are null
     * @throws IllegalArgumentException if one the above rules are broken
     */
    public Transaction(String id, String description, BigDecimal value, TransactionSubject subject, TransactionParty party) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(value, "value cannot be null");
        Objects.requireNonNull(subject, "subject cannot be null");
        Objects.requireNonNull(party, "party cannot be null");

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must be above 0");
        }

        Set<TransactionPartyType> allowedPartiesForSubject = getAllowedPartiesForSubject(subject);
        if (!allowedPartiesForSubject.contains(party.getType())) {
            throw new IllegalArgumentException("Party type is not allowed for subject");
        }

        this.id = id;
        this.description = description;
        this.value = value;
        this.subject = subject;
        this.party = party;
    }

    private Set<TransactionPartyType> getAllowedPartiesForSubject(TransactionSubject subject) {
        switch (subject) {
            case DONATION:
                return EnumSet.of(TransactionPartyType.DONOR, TransactionPartyType.COMPANY);
            case PAYMENT:
                return EnumSet.of(TransactionPartyType.EMPLOYEE, TransactionPartyType.SERVICE_PROVIDER, TransactionPartyType.COMPANY);
            case CHILD_PAYMENT:
                return EnumSet.of(TransactionPartyType.CITY_HALL);
            case CHILD_SALARY:
                return EnumSet.of(TransactionPartyType.CHILD);
            default:
                return EnumSet.noneOf(TransactionPartyType.class);
        }
    }

    /**
     * Returns the Id of the Transaction.
     *
     * @return String representing the id of the Transaction.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the Description of the Transaction.
     *
     * @return String representing the Description of the Transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the Value of the Transaction.
     *
     * @return BigDecimal representing the value of the Transaction.
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Returns the Subject of the Transaction.
     *
     * @return TransactionSubject representing the type of the Transaction.
     */
    public TransactionSubject getSubject() {
        return subject;
    }

    /**
     * Returns the Party of the Transaction.
     *
     * @return TransactionParty representing the party of the Transaction.
     */
    public TransactionParty getParty() {
        return party;
    }

    /**
     * Returns the amount of the Transaction (same as getValue()).
     *
     * @return BigDecimal representing the value of the Transaction.
     */
    public BigDecimal getAmount() {
        return value;
    }
}
