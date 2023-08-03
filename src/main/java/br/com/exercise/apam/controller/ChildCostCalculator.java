package br.com.exercise.apam.controller;

import java.math.BigDecimal;

import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;

/**
 * This Class Calculates the Cost of each child
 */
public final class ChildCostCalculator {

    private final AccountController accountController;

    /**
     * Build a new ChildCostCalculator
     *
     * @param accountController AccountController to retrieve child accounts
     */
    public ChildCostCalculator(AccountController accountController) {
        this.accountController = accountController;
    }

    /**
     * This method calculates the monthly cost of a child based on the following rules.
     * 1. R$150 for each year that the child has (ex. 10 years = R$ 1500).
     * 2. If the child age is 5 years old or below, add R$100 for each year (ex. 4 years = R$1000).
     * 3. If the child has a bank account, deduct 10% of the total balance from the payment (ex. 10 years + R$1000 on bank account = R$ 1400).
     * 4. The Result should be rounded to 2 decimal places with Floor Precision (ex. R$2.225 = R$2.22)
     *
     * @param transactionParty TransactionParty representing the child that the cost will be calculated.
     *
     * @return BigDecimal representing the monthly cost of the child.
     *
     * @throws IllegalArgumentException if the Person is not a Child.
     */
    public BigDecimal calculateMonthlyCost(final TransactionParty transactionParty) {
        if (transactionParty.getType() != TransactionPartyType.CHILD) {
            throw new IllegalArgumentException("TransactionParty type is not allowed");
        }

        int age = transactionParty.getAge();
        BigDecimal baseCost = BigDecimal.valueOf(150).multiply(BigDecimal.valueOf(age));

        if (age <= 5) {
            baseCost = baseCost.add(BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(age)));
        }

        BigDecimal accountDeduction = BigDecimal.ZERO;
        Account account = accountController.getAccount(transactionParty);

        if (account != null) {
            BigDecimal accountBalance = account.getBalance();
            BigDecimal deduction = accountBalance.multiply(BigDecimal.valueOf(0.10));
            accountDeduction = deduction.min(baseCost);
        }

        BigDecimal monthlyCost = baseCost.subtract(accountDeduction);
        return monthlyCost.setScale(2, BigDecimal.ROUND_FLOOR);
    }
}
