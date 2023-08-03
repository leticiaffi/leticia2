package br.com.exercise.apam.dataprovider;

import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class CommonsProvider {

    private static final Faker FAKER = new Faker();

    private CommonsProvider() {
    }

    public static String provideId() {
        return UUID.randomUUID().toString();
    }

    public static String provideDescription() {
        return FAKER.funnyName().name();
    }

    public static BigDecimal provideValue() {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 10000));
    }

    public static String provideName() {
        return FAKER.name().fullName();
    }

    public static Integer provideAge() {
        return ThreadLocalRandom.current().nextInt(1, 100);
    }
}
