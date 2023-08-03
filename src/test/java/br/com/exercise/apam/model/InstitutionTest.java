package br.com.exercise.apam.model;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideAge;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideName;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;

@CamelCaseDisplayNameGenerator
class InstitutionTest {

    @ParameterizedTest
    @EnumSource(
            value = TransactionPartyType.class,
            names = {"CITY_HALL", "COMPANY"}
    )
    void shouldBuildAllAllowedTypes(final TransactionPartyType transactionPartyType) {
        assertDoesNotThrow(() -> new Institution(provideId(), provideName(), provideAge(), transactionPartyType));
    }

    @ParameterizedTest
    @EnumSource(
            value = TransactionPartyType.class,
            names = {"CITY_HALL", "COMPANY"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void shouldThrowExceptionForNotAllowedTypes(final TransactionPartyType transactionPartyType) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Institution(provideId(), provideName(), provideAge(), transactionPartyType));

        assertEquals("Type is not allowed for Institution", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Institution(null, provideName(), provideAge(), TransactionPartyType.COMPANY));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Institution(provideId(), null, provideAge(), TransactionPartyType.COMPANY));
    }

    @Test
    void shouldThrowExceptionWhenAgeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Institution(provideId(), provideName(), null, TransactionPartyType.COMPANY));
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> new Institution(provideId(), provideName(), provideAge(), null));
    }

    @Test
    void shouldReturnId() {
        final String id = provideId();
        final Institution institution = new Institution(id, provideName(), provideAge(), TransactionPartyType.COMPANY);

        assertEquals(id, institution.getId());
    }

    @Test
    void shouldReturnName() {
        final String name = provideName();
        final Institution institution = new Institution(provideId(), name, provideAge(), TransactionPartyType.COMPANY);

        assertEquals(name, institution.getName());
    }

    @Test
    void shouldReturnType() {
        final Institution institution = new Institution(provideId(), provideName(), provideAge(), TransactionPartyType.COMPANY);

        assertEquals(TransactionPartyType.COMPANY, institution.getType());
    }

    @Test
    void shouldBeATransactionParty() {
        final Institution institution = new Institution(provideId(), provideName(), provideAge(), TransactionPartyType.COMPANY);

        assertTrue(institution instanceof TransactionParty);
    }
}