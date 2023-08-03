package br.com.exercise.apam.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import br.com.exercise.apam.helpers.CamelCaseDisplayNameGenerator;

@CamelCaseDisplayNameGenerator
class TransactionSubjectTest {

    @ParameterizedTest
    @EnumSource(
            value = TransactionSubject.class,
            names = {"DONATION"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void shouldNotAllowAnonymousPayments(final TransactionSubject type) {
        assertFalse(type.isAnonymousAllowed());
    }

    @ParameterizedTest
    @EnumSource(
            value = TransactionSubject.class,
            names = {"DONATION"}
    )
    void shouldAllowAnonymousPayments(final TransactionSubject type) {
        assertTrue(type.isAnonymousAllowed());
    }

    @ParameterizedTest
    @EnumSource(
            value = TransactionSubject.class,
            names = {"PAYMENT"},
            mode = EnumSource.Mode.EXCLUDE
    )
    void shouldHaveInType(final TransactionSubject type) {
        assertEquals(TransactionType.IN, type.getTransactionType());
    }

    @ParameterizedTest
    @EnumSource(
            value = TransactionSubject.class,
            names = {"PAYMENT"}
    )
    void shouldHaveOutType(final TransactionSubject type) {
        assertEquals(TransactionType.OUT, type.getTransactionType());
    }
}