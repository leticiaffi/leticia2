package br.com.exercise.apam.model;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This class represents a Person that interacts with Apam.
 */
public class Person implements TransactionParty {
    private final String id;
    private final String name;
    private final Integer age;
    private final TransactionPartyType type;

    /**
     * Builds a new anonymous Person, setting the name to "Anonymous", a Random Generated UUID for id and 0 for age.
     *
     * @param type Type of the person
     *
     * @throws IllegalArgumentException if name is anonymous and type is not Donor
     * @throws NullPointerException if type parameter is null
     */
    public Person(TransactionPartyType type) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(type, "type cannot be null");

        if (type == TransactionPartyType.DONOR) {
            this.name = "Anonymous";
        } else {
            throw new IllegalArgumentException("Anonymous type is only allowed for DONOR type");
        }

        this.id = UUID.randomUUID().toString();
        this.age = 0;
        this.type = type;
    }

    /**
     * Builds a new Person following these rules:
     * 1. Anonymous is only allowed to DONOR type
     * 2. Only these types are allowed for Person: [CHILD, EMPLOYEE, DONOR, SERVICE_PROVIDER]
     *
     * @param id Id of the person
     * @param name Name of the person
     * @param age Age of the person
     * @param type Type of the person
     *
     * @throws IllegalArgumentException if name is anonymous and type is not Donor
     * @throws NullPointerException if any of the parameters are null
     */
    public Person(String id, String name, Integer age, TransactionPartyType type) throws IllegalArgumentException, NullPointerException {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(age, "age cannot be null");
        Objects.requireNonNull(type, "type cannot be null");

        Set<TransactionPartyType> allowedTypes = ImmutableSet.of(TransactionPartyType.CHILD, TransactionPartyType.EMPLOYEE, TransactionPartyType.DONOR, TransactionPartyType.SERVICE_PROVIDER);
        if (!allowedTypes.contains(type)) {
            throw new IllegalArgumentException("Type is not allowed for Person");
        }
        if ("Anonymous".equalsIgnoreCase(name) && type != TransactionPartyType.DONOR) {
            throw new IllegalArgumentException("Anonymous type is only allowed for DONOR type");
        }
        this.id = id;
        this.name = name;
        this.age = age;
        this.type = type;
    }

    /**
     * Return the Person Id.
     *
     * @return String representing the id of the Person.
     */
    public String getId() {
        return id;
    }

    /**
     * Return the Person Name.
     *
     * @return String representing the Name of the Person.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the Person Age.
     *
     * @return Integer representing the Age of the Person.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Return the Type of the Person.
     *
     * @return TransactionPartyType related to this Person.
     */
    public TransactionPartyType getType() {
        return type;
    }
}
