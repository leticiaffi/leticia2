package br.com.exercise.apam.model;

import static br.com.exercise.apam.model.TransactionPartyType.CITY_HALL;
import static br.com.exercise.apam.model.TransactionPartyType.COMPANY;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Set;

/**
 * This class Represents an institution that iteracts with Apam
 */
public class Institution implements TransactionParty {

    private final String id;
    private final String name;
    private final Integer age;
    private final TransactionPartyType type;

    /**
     * Builds a new Institution following these rules:
     * 1. Only these types are allowed for Institution: [CITY_HALL, COMPANY]
     *
     * @param id Id of the Institution.
     * @param name Name of the Institution.
     * @param type Type of the Institution.
     *
     * @throws NullPointerException if any of the parameters are null.
     */
    public Institution(String id, String name, Integer age, TransactionPartyType type) {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(age, "age cannot be null");
        Objects.requireNonNull(type, "type cannot be null");

        Set<TransactionPartyType> allowedTypes = ImmutableSet.of(CITY_HALL, COMPANY);
        if (!allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Type is not allowed for Institution");
        }

        this.id = id;
        this.name = name;
        this.age = age;
        this.type = type;
    }

    /**
     * Returns the Id of the Institution.
     *
     * @return String representing the Id of the Institution.
     */
    public String getId() {
        return id;

    }

    /**
     * Returns the Name of the institution
     *
     * @return String representing the Name of the Institution.
     */
    public String getName() {
        return name;

    }

    /**
     * Returns the Age of the Institution
     *
     * @return Integer Representing the Age of the Institution.
     */
    public Integer getAge() {
        return age;

    }

    /**
     * Returns the type of the Institution.
     *
     * @return TransactionPartyType representing the type of the Institution.
     */
    public TransactionPartyType getType() {
        return type;

    }
}
