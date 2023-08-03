package br.com.exercise.apam.dataprovider;

import static br.com.exercise.apam.model.TransactionPartyType.CITY_HALL;
import static br.com.exercise.apam.model.TransactionPartyType.COMPANY;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionPartyType;

public final class TransactionPartyDataProvider {

    private static final Set<TransactionPartyType> INSTITUTION_PARTY_TYPES = ImmutableSet.of(
            CITY_HALL, COMPANY
    );

    private TransactionPartyDataProvider() {
    }

    public static TransactionParty provide(final TransactionPartyType type) {
        if (INSTITUTION_PARTY_TYPES.contains(type)) {
            return InstitutionDataProvider.provide(type);
        } else {
            return PersonDataProvider.provide(type);
        }
    }
}
