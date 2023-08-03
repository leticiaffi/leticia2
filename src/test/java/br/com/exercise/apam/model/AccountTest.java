package br.com.exercise.apam.model;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideDescription;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.exercise.apam.dataprovider.TransactionDataProvider;
import br.com.exercise.apam.dataprovider.TransactionPartyDataProvider;
import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;

@CamelCaseDisplayNameGenerator
class AccountTest {

    @ParameterizedTest
    @EnumSource(AccountType.class)
    void shouldBuildAccountForAllTypes(final AccountType type) {
        final Account account = assertDoesNotThrow(
                () -> new Account(provideId(), provideDescription(), type));

        assertNotNull(account);
        assertNotNull(account.getTransactions());
        assertTrue(account.getTransactions().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Account(null, provideDescription(), AccountType.DONATION_ACCOUNT));
    }

    @Test
    void shouldThrowExceptionWhenNumberIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Account(provideId(), null, AccountType.DONATION_ACCOUNT));
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Account(provideId(), provideDescription(), null));
    }

    @Test
    void shouldReturnId() {
        final String id = provideId();
        final Account account = new Account(id, provideDescription(), AccountType.DONATION_ACCOUNT);

        assertEquals(id, account.getId());
    }

    @Test
    void shouldReturnDescription() {
        final String number = provideDescription();
        final Account account = new Account(provideId(), number, AccountType.DONATION_ACCOUNT);

        assertEquals(number, account.getNumber());
    }

    @Test
    void shouldReturnType() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);

        assertEquals(AccountType.DONATION_ACCOUNT, account.getType());
    }

    @Test
    void shouldReturnTransactionsEmpty() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);

        assertTrue(account.getTransactions().isEmpty());
    }

    @Test
    void shouldReturnTransactions() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);
        final Transaction transaction = TransactionDataProvider.provide(
                TransactionSubject.DONATION,
                TransactionPartyDataProvider.provide(TransactionPartyType.DONOR));

        account.addTransaction(transaction);

        assertEquals(1, account.getTransactions().size());
        assertEquals(transaction, account.getTransactions().get(0));
    }

    @Test
    void shouldReturnBalanceZero() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);

        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void shouldReturnBalance() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);
        final Transaction transaction1 = TransactionDataProvider.provide(
                TransactionSubject.DONATION,
                TransactionPartyDataProvider.provide(TransactionPartyType.DONOR));
        final Transaction transaction2 = TransactionDataProvider.provide(
                TransactionSubject.DONATION,
                TransactionPartyDataProvider.provide(TransactionPartyType.DONOR));

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        assertEquals(transaction1.getValue().add(transaction2.getValue()), account.getBalance());
    }

    @Test
    void shouldAddTransaction() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);
        final Transaction transaction = TransactionDataProvider.provide(
                TransactionSubject.DONATION,
                TransactionPartyDataProvider.provide(TransactionPartyType.DONOR));

        account.addTransaction(transaction);

        assertEquals(1, account.getTransactions().size());
        assertEquals(transaction, account.getTransactions().get(0));
    }

    @Test
    void shouldNotAddDuplicatedTransaction() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);
        final Transaction transaction = TransactionDataProvider.provide(
                TransactionSubject.DONATION,
                TransactionPartyDataProvider.provide(TransactionPartyType.DONOR));

        account.addTransaction(transaction);
        account.addTransaction(transaction);

        assertEquals(1, account.getTransactions().size());
        assertEquals(transaction, account.getTransactions().get(0));
    }

    @Test
    void shouldThrowExceptionWhenAddingNullTransaction() {
        final Account account = new Account(provideId(), provideDescription(), AccountType.DONATION_ACCOUNT);

        assertThrows(NullPointerException.class, () -> account.addTransaction(null));
    }

    @ParameterizedTest
    @MethodSource(value = "provideValidTransactions")
    void shouldNotThrowExceptionWhenAddingValidTransaction(final AccountType accountType,
                                                           final TransactionSubject transactionSubject,
                                                           final TransactionPartyType transactionPartyType) {

        final Account account = new Account(provideId(), provideDescription(), accountType);
        final Transaction transaction = TransactionDataProvider.provide(
                transactionSubject,
                TransactionPartyDataProvider.provide(transactionPartyType));

        assertDoesNotThrow(() -> account.addTransaction(transaction));
    }

    @ParameterizedTest
    @MethodSource(value = "provideInvalidTransactions")
    void shouldThrowExceptionWhenAddingInvalidTransaction(final AccountType accountType,
                                                          final TransactionSubject transactionSubject,
                                                          final TransactionPartyType transactionPartyType) {
        final Account account = new Account(provideId(), provideDescription(), accountType);
        final Transaction transaction = TransactionDataProvider.provide(
                transactionSubject,
                TransactionPartyDataProvider.provide(transactionPartyType));

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> account.addTransaction(transaction));

        assertEquals("Invalid Transaction for Account", exception.getMessage());
    }

    private static Stream<Arguments> provideValidTransactions() {
        return Stream.of(
                Arguments.of(AccountType.CITY_HALL_ACCOUNT, TransactionSubject.CHILD_PAYMENT, TransactionPartyType.CITY_HALL),
                Arguments.of(AccountType.CITY_HALL_ACCOUNT, TransactionSubject.PAYMENT, TransactionPartyType.SERVICE_PROVIDER),
                Arguments.of(AccountType.CHILD_ACCOUNT, TransactionSubject.CHILD_SALARY, TransactionPartyType.COMPANY),
                Arguments.of(AccountType.CHILD_ACCOUNT, TransactionSubject.PAYMENT, TransactionPartyType.COMPANY),
                Arguments.of(AccountType.DONATION_ACCOUNT, TransactionSubject.DONATION, TransactionPartyType.DONOR),
                Arguments.of(AccountType.DONATION_ACCOUNT, TransactionSubject.PAYMENT, TransactionPartyType.COMPANY)
        );
    }

    private static Stream<Arguments> provideInvalidTransactions() {
        return Stream.of(
                Arguments.of(AccountType.CITY_HALL_ACCOUNT, TransactionSubject.DONATION, TransactionPartyType.DONOR),
                Arguments.of(AccountType.CITY_HALL_ACCOUNT, TransactionSubject.CHILD_SALARY, TransactionPartyType.COMPANY),
                Arguments.of(AccountType.CHILD_ACCOUNT, TransactionSubject.CHILD_PAYMENT, TransactionPartyType.CITY_HALL),
                Arguments.of(AccountType.CHILD_ACCOUNT, TransactionSubject.DONATION, TransactionPartyType.DONOR),
                Arguments.of(AccountType.DONATION_ACCOUNT, TransactionSubject.CHILD_PAYMENT, TransactionPartyType.CITY_HALL),
                Arguments.of(AccountType.DONATION_ACCOUNT, TransactionSubject.CHILD_SALARY, TransactionPartyType.COMPANY)
        );
    }
}