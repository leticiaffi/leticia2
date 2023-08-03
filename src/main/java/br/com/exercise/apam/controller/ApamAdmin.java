package br.com.exercise.apam.controller;

import java.math.BigDecimal;
import java.util.*;

import br.com.exercise.apam.model.Transaction;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;

/**
 * Controls all operations on Apam
 */
public class ApamAdmin {

    private List<TransactionParty> transactionPartyList;
    private Map<String, List<TransactionParty>> cityHallChildren;

    private final AccountController accountController;
    private final ChildCostCalculator childCostCalculator;

    /**
     * Builds a new ApamAdmin object with the following objects:
     * 1. accountController
     * 2. childCostController
     * 3. transactionPartyList
     * 4. cityHallChildren
     */
    public ApamAdmin(final AccountController accountController) {
        this.accountController = accountController;
        this.childCostCalculator = new ChildCostCalculator(accountController);
        this.transactionPartyList = new ArrayList<>();
        this.cityHallChildren = new HashMap<>();
    }

    /**
     * Returns the List of TransactionParty
     *
     * @return List of TransactionParty
     */
    public List<TransactionParty> getTransactionPartyList() {
        return transactionPartyList;
    }

    /**
     * Return the list of children registered for a city hall with the following rules:
     * 1. Throw IllegalArgumentException if the TransactionParty is not a City Hall
     * 2. Throw NullPointerException if the TransactionParty is null
     * 3. Throw IllegalArgumentException if the City Hall is not registered in the transaction party list
     *
     * @param cityHall TransactionParty representing the city hall
     *
     * @return List of TransactionParty of empty list if not found
     *
     * @throws IllegalArgumentException if the TransactionParty is not a City Hall
     * @throws NullPointerException if the TransactionParty is null
     */
    public List<TransactionParty> getCityHallChildren(final TransactionParty cityHall) {
        Objects.requireNonNull(cityHall, "TransactionParty cannot be null");

        if (cityHall.getType() != TransactionPartyType.CITY_HALL) {
            throw new IllegalArgumentException("TransactionParty is not CityHall");
        }
        if (!transactionPartyList.contains(cityHall)){
            throw new IllegalArgumentException("City Hall it not in TransactionPartyList");
        }
        return cityHallChildren.getOrDefault(cityHall.getId(), new ArrayList<>());
    }

    /**
     * Adds a new Transaction Party to the Available ones following these rules:
     * 1. If the transaction party already exists, ignore it
     *
     * @param transactionParty TransactionParty to be added
     *
     * @throws NullPointerException if the TransactionParty is null
     */
    public void addTransactionParty(final TransactionParty transactionParty) throws NullPointerException {
        Objects.requireNonNull(transactionParty, "TransactionParty cannot be null");

        if (!transactionPartyList.contains(transactionParty)) {
            transactionPartyList.add(transactionParty);
        }
    }

    /**
     * Add a child to a transaction party following these rules:
     * 1. If the CityHall parameter is not of CITY_HALL type, throw IllegalArgumentException
     * 2. If the Child parameter is not of CHILD type, throw IllegalArgumentException
     * 3. If the Child is already added to another City Hall, throw IllegalArgumentException
     * 4. If any of the parameters are null, throw NullPointerException
     * 5. If the CityHall or Child is not registered in the Transaction Party List, throw IllegalArgumentException
     * 6. If the City Hall is registered and the child is not added to another City Hall, add it to the CityHallChildrenMap
     *
     * @param cityHall TransactionParty representing the City Hall
     * @param child TransactionParty representing the Child
     *
     * @throws IllegalArgumentException If rules 1, 2, 4 are violated
     * @throws NullPointerException If any of the parameters are null
     */
    public void addChild(final TransactionParty cityHall, final TransactionParty child) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(cityHall, "CityHall cannot be null");
        Objects.requireNonNull(child, "Child cannot be null");

        if (cityHall.getType() != TransactionPartyType.CITY_HALL) {
            throw new IllegalArgumentException("TransactionParty CityHall is not registered");
        }
        if (child.getType() != TransactionPartyType.CHILD) {
            throw new IllegalArgumentException("Child must be of type CHILD");
        }
        if (!transactionPartyList.contains(cityHall) || !transactionPartyList.contains(child)) {
            throw new IllegalArgumentException("TransactionParty CityHall is not registered");
        }
        if (cityHallChildren.values().stream().anyMatch(list -> list.contains(child))) {
            throw new IllegalArgumentException("Child Already Added to Another City Hall");
        }

        cityHallChildren.computeIfAbsent(cityHall.getId(), k -> new ArrayList<>()).add(child);
    }

    /**
     * Add a new Transaction
     *
     * @param source TransactionParty representing the source
     * @param transaction Transaction to be added
     *
     * @throws NullPointerException if any of the parameters are null
     */
    public void addTransaction(final TransactionParty source, final Transaction transaction) throws NullPointerException {
        Objects.requireNonNull(source, "Source cannot be null");
        Objects.requireNonNull(transaction, "Transaction cannot be null");

        accountController.addTransaction(source, transaction);
    }

    /**
     * Calculates the city hall payment following these rules:
     * 1. If the CityHall parameter is not of CITY_HALL type, throw IllegalArgumentException
     *
     * @param cityHall TransactionParty representing the City Hall
     *
     * @return BigDecimal representing the value of the payment
     *
     * @throws NullPointerException If any of the parameters are null
     * @throws IllegalArgumentException If the transaction party is not of city hall type
     */
    public BigDecimal calculateCityHallPayment(final TransactionParty cityHall) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(cityHall, "CityHall cannot be null");

        if (cityHall.getType() != TransactionPartyType.CITY_HALL) {
            throw new IllegalArgumentException("TransactionParty is not CityHall");
        }

        List<TransactionParty> children = getCityHallChildren(cityHall);
        BigDecimal totalPayment = BigDecimal.ZERO;

        for (TransactionParty child : children) {
            BigDecimal childPayment = childCostCalculator.calculateMonthlyCost(child);
            totalPayment = totalPayment.add(childPayment);
        }

        return totalPayment;
    }
}

