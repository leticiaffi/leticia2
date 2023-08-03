package br.com.exercise.apam.controller;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.exercise.apam.dataprovider.AccountDataProvider;
import br.com.exercise.apam.dataprovider.TransactionPartyDataProvider;
import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;
import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.AccountType;
import br.com.exercise.apam.model.Person;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;

@CamelCaseDisplayNameGenerator
class ChildCostCalculatorTest {

    private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.FLOOR);
    private static final Double ACCOUNT_DEDUCTION_PERCENT = 0.10;

    private AccountController accountController;
    private ChildCostCalculator childCostCalculator;

    @BeforeEach
    void setUp() {
        this.accountController = new AccountController();
        this.childCostCalculator = new ChildCostCalculator(accountController);
    }

    @Test
    void shouldThrowExceptionWhenTransactionPartyIsNotChild() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> childCostCalculator.calculateMonthlyCost(TransactionPartyDataProvider.provide(TransactionPartyType.DONOR)));

        assertEquals("TransactionParty type is not allowed", exception.getMessage());
    }

    @Test
    void shouldCalculateTheCostOfTenYearOldChildWithoutAccount() {
        final TransactionParty child = buildChild(10);
        final BigDecimal value = childCostCalculator.calculateMonthlyCost(child);

        assertEquals(BigDecimal.valueOf(1500).round(MATH_CONTEXT), value);
    }

    @Test
    void shouldCalculateCostOfFourYearOldChildWithoutAccount() {
        final TransactionParty child = buildChild(4);
        final BigDecimal value = childCostCalculator.calculateMonthlyCost(child);

        assertEquals(BigDecimal.valueOf(1000).round(MATH_CONTEXT), value);
    }

    @Test
    void shouldCalculateCostOf11YearOldWithAccount() {
        final TransactionParty child = buildChild(11);
        final Account account = AccountDataProvider.provide(AccountType.CHILD_ACCOUNT);

        accountController.addAccount(child, account);
        final BigDecimal value = childCostCalculator.calculateMonthlyCost(child);

        final BigDecimal expectedValue = BigDecimal.valueOf(1600)
                                                   .subtract(account.getBalance().multiply(BigDecimal.valueOf(ACCOUNT_DEDUCTION_PERCENT)))
                                                   .round(MATH_CONTEXT);
        assertEquals(expectedValue, value);
    }

    @Test
    void shouldCalculateCostOf3YearOldWithAccount() {
        final TransactionParty child = buildChild(3);
        final Account account = AccountDataProvider.provide(AccountType.CHILD_ACCOUNT);

        accountController.addAccount(child, account);
        final BigDecimal value = childCostCalculator.calculateMonthlyCost(child);

        final BigDecimal expectedValue = BigDecimal.valueOf(750)
                                                   .subtract(account.getBalance().multiply(BigDecimal.valueOf(ACCOUNT_DEDUCTION_PERCENT)))
                                                   .round(MATH_CONTEXT);
        assertEquals(expectedValue, value);
    }

    private TransactionParty buildChild(final Integer age) {
        return new Person(
                provideId(),
                provideName(),
                age,
                TransactionPartyType.CHILD);
    }
}