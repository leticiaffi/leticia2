package br.com.exercise.apam.model;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideAge;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideName;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;

@CamelCaseDisplayNameGenerator
class PersonTest {

    @ParameterizedTest
    @EnumSource(
            value = TransactionPartyType.class,
            names = {"CITY_HALL", "COMPANY"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void shouldBuildPersonForAllAllowedTypes(final TransactionPartyType transactionPartyType) {
        assertDoesNotThrow(() -> new Person(provideId(), provideName(), provideAge(), transactionPartyType));
    }

    @ParameterizedTest
    @EnumSource(
            value = TransactionPartyType.class,
            names = {"CITY_HALL", "COMPANY"}
    )
    void shouldThrowExceptionForNotAllowedTypes(final TransactionPartyType transactionPartyType) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Person(provideId(), provideName(), provideAge(), transactionPartyType));

        assertEquals("Type is not allowed for Person", exception.getMessage());
    }

    @Test
    void shouldBuildPersonWhenPersonIsAnonymousAndDonor() {
        assertDoesNotThrow(() -> new Person(provideId(), "Anonymous", provideAge(), TransactionPartyType.DONOR));
    }

    @Test
    void shouldThrowExceptionWhenPersonIsAnonymousAndNotDonor() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Person(provideId(), "Anonymous", provideAge(), TransactionPartyType.CHILD));

        assertEquals("Anonymous type is only allowed for DONOR type", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Person(null, provideName(), provideAge(), TransactionPartyType.CHILD));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Person(provideId(), null, provideAge(), TransactionPartyType.CHILD));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Person(provideId(), provideName(), provideAge(), null));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAgeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Person(provideId(), provideName(), null, TransactionPartyType.CHILD));
    }

    @Test
    void shouldBuildAnonymousPerson() {
        final Person person = assertDoesNotThrow(() -> new Person(TransactionPartyType.DONOR));

        assertNotNull(UUID.fromString(person.getId()));
        assertEquals("Anonymous", person.getName());
        assertEquals(0, person.getAge());
        assertEquals(TransactionPartyType.DONOR, person.getType());
    }

    @Test
    void shouldThrowExceptionWhenAnonymousPersonIsNotDonor() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Person(TransactionPartyType.CHILD));

        assertEquals("Anonymous type is only allowed for DONOR type", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAnonymousPersonTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Person(null));
    }

    @Test
    void shouldReturnPersonId() {
        final String id = provideId();
        final Person person = new Person(id, provideName(), provideAge(), TransactionPartyType.CHILD);

        assertEquals(id, person.getId());
    }

    @Test
    void shouldReturnPersonName() {
        final String name = provideName();
        final Person person = new Person(provideId(), name, provideAge(), TransactionPartyType.CHILD);
        assertEquals(name, person.getName());
    }

    @Test
    void shouldReturnPersonAge() {
        final Integer age = provideAge();
        final Person person = new Person(provideId(), provideName(), age, TransactionPartyType.CHILD);

        assertEquals(age, person.getAge());
    }

    @Test
    void shouldReturnPersonType() {
        final Person person = new Person(provideId(), provideName(), provideAge(), TransactionPartyType.CHILD);

        assertEquals(TransactionPartyType.CHILD, person.getType());
    }

    @Test
    void shouldBeATransactionParty() {
        final Person person = new Person(provideId(), provideName(), provideAge(), TransactionPartyType.CHILD);

        assertTrue(person instanceof TransactionParty);
    }
}