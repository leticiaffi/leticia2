package br.com.exercise.apam.dataprovider;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideAge;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideName;
import static br.com.exercise.apam.model.TransactionPartyType.CITY_HALL;

import br.com.exercise.apam.model.Institution;
import br.com.exercise.apam.model.TransactionPartyType;

public final class InstitutionDataProvider {

    private InstitutionDataProvider() {
    }

    public static Institution provideCityHall() {
        return provide(CITY_HALL);
    }

    public static Institution provide(final TransactionPartyType transactionPartyType) {
        return new Institution(provideId(), provideName(), provideAge(), transactionPartyType);
    }

}
