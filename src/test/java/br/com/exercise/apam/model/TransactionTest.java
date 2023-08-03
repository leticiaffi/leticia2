package br.com.exercise.apam.model;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideDescription;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideValue;
import static br.com.exercise.apam.dataprovider.PersonDataProvider.provideDonor;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import br.com.exercise.apam.dataprovider.TransactionPartyDataProvider;
import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;

@CamelCaseDisplayNameGenerator
class TransactionTest {

    @ParameterizedTest
    @MethodSource(value = "provideValidSubjectParties")
    void shouldBuildForAllTransactionTypes(final TransactionSubject subject, final TransactionPartyType partyType) {
        assertDoesNotThrow(() -> new Transaction(
                provideId(),
                provideDescription(),
                provideValue(),
                subject,
                TransactionPartyDataProvider.provide(partyType)));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Transaction(null, provideDescription(), provideValue(), TransactionSubject.DONATION, provideDonor()));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDescriptionIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Transaction(provideId(), null, provideValue(), TransactionSubject.DONATION, provideDonor()));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenValueIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Transaction(provideId(), provideDescription(), null, TransactionSubject.DONATION, provideDonor()));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenSubjectIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Transaction(provideId(), provideDescription(), provideValue(), null, provideDonor()));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenPartyIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Transaction(provideId(), provideDescription(), provideValue(), TransactionSubject.DONATION, null));
    }

    @Test
    void shouldReturnId() {
        final String id = provideId();
        final Transaction transaction = new Transaction(
                id,
                provideDescription(),
                provideValue(),
                TransactionSubject.DONATION,
                provideDonor());

        assertEquals(id, transaction.getId());
    }

    @Test
    void shouldReturnDescription() {
        final String description = provideDescription();
        final Transaction transaction = new Transaction(
                provideId(),
                description,
                provideValue(),
                TransactionSubject.DONATION,
                provideDonor());

        assertEquals(description, transaction.getDescription());
    }

    @Test
    void shouldReturnValue() {
        final BigDecimal value = provideValue();
        final Transaction transaction = new Transaction(
                provideId(),
                provideDescription(),
                value,
                TransactionSubject.DONATION,
                provideDonor());

        assertEquals(value, transaction.getValue());
    }

    @Test
    void shouldReturnSubject() {
        final Transaction transaction = new Transaction(
                provideId(),
                provideDescription(),
                provideValue(),
                TransactionSubject.DONATION,
                provideDonor());

        assertEquals(TransactionSubject.DONATION, transaction.getSubject());
    }

    @Test
    void shouldReturnParty() {
        final Person party = provideDonor();
        final Transaction transaction = new Transaction(
                provideId(),
                provideDescription(),
                provideValue(),
                TransactionSubject.DONATION,
                party);

        assertEquals(party, transaction.getParty());
    }

    @ParameterizedTest
    @MethodSource(value = "provideInvalidSubjectParties")
    void shouldThrowExceptionWhenBuildingWithInvalidSubjectParty(final TransactionSubject subject,
                                                                 final TransactionPartyType partyType) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Transaction(
                        provideId(),
                        provideDescription(),
                        provideValue(),
                        subject,
                        TransactionPartyDataProvider.provide(partyType)));

        assertEquals("Party type is not allowed for subject", exception.getMessage());
    }

    private static Stream<Arguments> provideInvalidSubjectParties() {
        return Stream.of(
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.CHILD),
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.EMPLOYEE),
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.SERVICE_PROVIDER),
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.CITY_HALL),

                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.CHILD),
                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.DONOR),
                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.CITY_HALL),

                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.CHILD),
                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.EMPLOYEE),
                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.DONOR),
                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.SERVICE_PROVIDER),
                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.COMPANY),

                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.EMPLOYEE),
                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.DONOR),
                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.SERVICE_PROVIDER),
                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.CHILD),
                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.CITY_HALL)
        );
    }

    private static Stream<Arguments> provideValidSubjectParties() {
        return Stream.of(
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.DONOR),
                Arguments.of(TransactionSubject.DONATION, TransactionPartyType.COMPANY),

                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.EMPLOYEE),
                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.SERVICE_PROVIDER),
                Arguments.of(TransactionSubject.PAYMENT, TransactionPartyType.COMPANY),

                Arguments.of(TransactionSubject.CHILD_PAYMENT, TransactionPartyType.CITY_HALL),

                Arguments.of(TransactionSubject.CHILD_SALARY, TransactionPartyType.COMPANY)
        );
    }
}