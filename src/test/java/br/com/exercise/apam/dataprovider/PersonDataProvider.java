package br.com.exercise.apam.dataprovider;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideAge;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideName;

import br.com.exercise.apam.model.Person;
import br.com.exercise.apam.model.TransactionPartyType;

public final class PersonDataProvider {

    private PersonDataProvider() {
    }

    public static Person provideDonor() {
        return provide(TransactionPartyType.DONOR);
    }

    public static Person provide(final TransactionPartyType transactionPartyType) {
        return new Person(provideId(), provideName(), provideAge(), transactionPartyType);
    }

}
