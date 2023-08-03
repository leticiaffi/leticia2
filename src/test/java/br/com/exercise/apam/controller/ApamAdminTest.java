package br.com.exercise.apam.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.exercise.apam.dataprovider.AccountDataProvider;
import br.com.exercise.apam.dataprovider.TransactionDataProvider;
import br.com.exercise.apam.dataprovider.TransactionPartyDataProvider;
import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;
import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.AccountType;
import br.com.exercise.apam.model.Transaction;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;
import br.com.exercise.apam.model.TransactionSubject;

@CamelCaseDisplayNameGenerator
class ApamAdminTest {

    private static final TransactionParty APAM = TransactionPartyDataProvider.provide(TransactionPartyType.COMPANY);
    private static final TransactionParty PITANGUI = TransactionPartyDataProvider.provide(TransactionPartyType.CITY_HALL);
    private static final TransactionParty MARTINHO_CAMPOS = TransactionPartyDataProvider.provide(TransactionPartyType.CITY_HALL);
    private static final TransactionParty DALSON_AREIA = TransactionPartyDataProvider.provide(TransactionPartyType.SERVICE_PROVIDER);
    private static final TransactionParty LUCIANO_CALCADOS = TransactionPartyDataProvider.provide(TransactionPartyType.COMPANY);
    private static final TransactionParty ROBSON = TransactionPartyDataProvider.provide(TransactionPartyType.DONOR);
    private static final TransactionParty FRANISCO = TransactionPartyDataProvider.provide(TransactionPartyType.CHILD);
    private static final TransactionParty ISIS = TransactionPartyDataProvider.provide(TransactionPartyType.CHILD);
    private static final Transaction DONATION = TransactionDataProvider.provide(TransactionSubject.DONATION, APAM);
    private static final Account APAM_DONATION_ACCOUNT = AccountDataProvider.provide(AccountType.DONATION_ACCOUNT);

    private AccountController accountController;
    private ApamAdmin apamAdmin;

    @BeforeEach
    void setUp() {
        this.accountController = new AccountController();
        this.apamAdmin = new ApamAdmin(this.accountController);
    }

    @Test
    void shouldReturnEmptyTransactionPartyList() {
        assertNotNull(apamAdmin.getTransactionPartyList());
        assertTrue(apamAdmin.getTransactionPartyList().isEmpty());
    }

    @Test
    void shouldReturnTransactionPartyList() {
        apamAdmin.addTransactionParty(PITANGUI);

        assertEquals(1, apamAdmin.getTransactionPartyList().size());
        assertEquals(PITANGUI, apamAdmin.getTransactionPartyList().get(0));
    }

    @Test
    void shouldThrowExceptionWhenCityHallIsNull() {
        assertThrows(NullPointerException.class, () -> apamAdmin.getCityHallChildren(null));
    }

    @Test
    void shouldThrowExceptionWhenCityHallIsNotOfCityHallType() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.getCityHallChildren(DALSON_AREIA));

        assertEquals("TransactionParty is not CityHall", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCityHallIsNotRegistered() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.getCityHallChildren(PITANGUI));

        assertEquals("City Hall it not in TransactionPartyList", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenCityHallHasNoChildren() {
        apamAdmin.addTransactionParty(PITANGUI);

        final List<TransactionParty> children = apamAdmin.getCityHallChildren(PITANGUI);
        assertNotNull(children);
        assertTrue(children.isEmpty());
    }

    @Test
    void shouldReturnCityHallChildren() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(FRANISCO);
        apamAdmin.addChild(PITANGUI, FRANISCO);

        final List<TransactionParty> children = apamAdmin.getCityHallChildren(PITANGUI);
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals(FRANISCO, children.get(0));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullTransactionParty() {
        assertThrows(NullPointerException.class, () -> apamAdmin.addTransactionParty(null));
    }

    @Test
    void shouldAddTransactionParty() {
        assertTrue(apamAdmin.getTransactionPartyList().isEmpty());

        apamAdmin.addTransactionParty(DALSON_AREIA);

        assertEquals(1, apamAdmin.getTransactionPartyList().size());
        assertEquals(DALSON_AREIA, apamAdmin.getTransactionPartyList().get(0));
    }

    @Test
    void shouldNotAddDuplicatedTransactionParty() {
        assertTrue(apamAdmin.getTransactionPartyList().isEmpty());

        apamAdmin.addTransactionParty(DALSON_AREIA);
        apamAdmin.addTransactionParty(DALSON_AREIA);

        assertEquals(1, apamAdmin.getTransactionPartyList().size());
        assertEquals(DALSON_AREIA, apamAdmin.getTransactionPartyList().get(0));
    }

    @Test
    void shouldThrowExceptionWhenAddingChildWithNullCityHall() {
        assertThrows(NullPointerException.class, () -> apamAdmin.addChild(null, FRANISCO));
    }

    @Test
    void shouldThrowExceptionWhenAddingChildWithNullChild() {
        assertThrows(NullPointerException.class, () -> apamAdmin.addChild(PITANGUI, null));
    }

    @Test
    void shouldThrowExceptionWhenAddingChildForNonRegisteredCityHall() {
        apamAdmin.addTransactionParty(FRANISCO);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.addChild(PITANGUI, FRANISCO));

        assertEquals("TransactionParty CityHall is not registered", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingChildForNonRegisteredChild() {
        apamAdmin.addTransactionParty(PITANGUI);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.addChild(PITANGUI, FRANISCO));

        assertEquals("TransactionParty Child is not registered", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddingChildThatIsAlreadyAddedToACityHall() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(MARTINHO_CAMPOS);
        apamAdmin.addTransactionParty(FRANISCO);

        apamAdmin.addChild(PITANGUI, FRANISCO);

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.addChild(MARTINHO_CAMPOS, FRANISCO));

        assertEquals("Child Already Added to Another City Hall", exception.getMessage());
    }

    @Test
    void shouldFirstAddChildToCityHall() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(FRANISCO);

        apamAdmin.addChild(PITANGUI, FRANISCO);

        assertEquals(1, apamAdmin.getCityHallChildren(PITANGUI).size());
        assertEquals(FRANISCO, apamAdmin.getCityHallChildren(PITANGUI).get(0));
    }

    @Test
    void shouldAddMultipleChildrenToCityHall() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(FRANISCO);
        apamAdmin.addTransactionParty(ISIS);

        apamAdmin.addChild(PITANGUI, FRANISCO);
        apamAdmin.addChild(PITANGUI, ISIS);

        assertEquals(2, apamAdmin.getCityHallChildren(PITANGUI).size());
        assertEquals(FRANISCO, apamAdmin.getCityHallChildren(PITANGUI).get(0));
        assertEquals(ISIS, apamAdmin.getCityHallChildren(PITANGUI).get(1));
    }

    @Test
    void shouldThrowExceptionWhenAddingTransactionPartyWithNullSource() {
        assertThrows(NullPointerException.class, () -> apamAdmin.addTransaction(null, DONATION));
    }

    @Test
    void shouldThrowExceptionWhenAddingTransactionPartyWithNullTransaction() {
        assertThrows(NullPointerException.class, () -> apamAdmin.addTransaction(ROBSON, null));
    }

    @Test
    void shouldAddTransactionToAccount() {
        accountController.addAccount(APAM, APAM_DONATION_ACCOUNT);

        apamAdmin.addTransaction(APAM, DONATION);

        final List<Transaction> transactions = accountController.getAccount(APAM).getTransactions();
        assertEquals(1, transactions.size());
        assertEquals(DONATION, transactions.get(0));
    }

    @Test
    void shouldThrowExceptionWhenCalculatingCityHallPaymentForNullCityHall() {
        assertThrows(NullPointerException.class, () -> apamAdmin.calculateCityHallPayment(null));
    }

    @Test
    void shouldThrowExceptionWhenCalculatingCityHallPaymentForNonCityHallType() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> apamAdmin.calculateCityHallPayment(APAM));

        assertEquals("TransactionParty is not CityHall", exception.getMessage());
    }

    @Test
    void shouldCalculatePaymentForCityHallWithoutChildren() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(LUCIANO_CALCADOS);

        final BigDecimal payment = apamAdmin.calculateCityHallPayment(PITANGUI);
        assertNotNull(payment);
        assertEquals(BigDecimal.ZERO, payment);
    }

    @Test
    void shouldCalculatePaymentForCityHallChildren() {
        apamAdmin.addTransactionParty(PITANGUI);
        apamAdmin.addTransactionParty(FRANISCO);
        apamAdmin.addTransactionParty(ISIS);
        apamAdmin.addTransactionParty(LUCIANO_CALCADOS);

        final Account franiscoAccount = AccountDataProvider.provide(AccountType.CHILD_ACCOUNT);
        accountController.addAccount(FRANISCO, franiscoAccount);
        apamAdmin.addTransaction(FRANISCO, TransactionDataProvider.provide(TransactionSubject.CHILD_SALARY, LUCIANO_CALCADOS));
        apamAdmin.addChild(PITANGUI, FRANISCO);
        apamAdmin.addChild(PITANGUI, ISIS);

        final ChildCostCalculator childCostCalculator = new ChildCostCalculator(this.accountController);
        BigDecimal expectedCost = BigDecimal.ZERO;
        expectedCost = expectedCost.add(childCostCalculator.calculateMonthlyCost(FRANISCO));
        expectedCost = expectedCost.add(childCostCalculator.calculateMonthlyCost(ISIS));

        final BigDecimal payment = apamAdmin.calculateCityHallPayment(PITANGUI);
        assertNotNull(payment);
        assertEquals(expectedCost, payment);
    }
}