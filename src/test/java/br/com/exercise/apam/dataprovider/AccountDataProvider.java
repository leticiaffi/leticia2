package br.com.exercise.apam.dataprovider;

import static br.com.exercise.apam.dataprovider.CommonsProvider.provideId;

import br.com.exercise.apam.model.Account;
import br.com.exercise.apam.model.AccountType;

public final class AccountDataProvider {

    private AccountDataProvider() {
    }

    public static Account provide() {
        return provide(AccountType.DONATION_ACCOUNT);
    }

    public static Account provide(final AccountType accountType) {
        return new Account(provideId(), provideId(), accountType);
    }

}
