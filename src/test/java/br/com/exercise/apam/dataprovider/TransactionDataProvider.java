package br.com.exercise.apam.dataprovider;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideDescription;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;
import static br.com.exercise.apam.dataprovider.CommonsProvider.provideValue;
import static br.com.exercise.apam.dataprovider.PersonDataProvider.provideDonor;

import br.com.exercise.apam.model.Transaction;
import br.com.exercise.apam.model.TransactionParty;
import br.com.exercise.apam.model.TransactionSubject;

public final class TransactionDataProvider {
    private TransactionDataProvider() {
    }

    public static Transaction providePersonDonation() {
        return provide(TransactionSubject.DONATION, provideDonor());
    }

    public static Transaction provide(final TransactionSubject transactionSubject, final TransactionParty transactionParty) {
        return new Transaction(provideId(), provideDescription(), provideValue(), transactionSubject, transactionParty);
    }
}
