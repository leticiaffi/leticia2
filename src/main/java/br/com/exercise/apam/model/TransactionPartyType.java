package br.com.exercise.apam.model;

/**
 * Represents the types of persons that can interact with Apam.
 */
public enum TransactionPartyType {
    /**
     * Childs are the persons that Apam takes care of.
     */
    CHILD,

    /**
     * Employees are the people that work at Apam to take care of the childs.
     */
    EMPLOYEE,

    /**
     * Donors are people that donate food or money to help Apam daily work.
     */
    DONOR,

    /**
     * Service providers that are not companies but provide services to Apam.
     */
    SERVICE_PROVIDER,

    /**
     * City Hall are the institutions that have contracts with Apam to take care or their childs.
     */
    CITY_HALL,

    /**
     * Companies are both service providers and donors to Apam.
     */
    COMPANY;
}
