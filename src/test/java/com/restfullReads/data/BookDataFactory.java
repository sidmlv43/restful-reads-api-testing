package com.restfullReads.data;

import com.github.javafaker.Faker;
import com.restfullReads.models.requests.CreateBookRequest;

public class BookDataFactory {

    private static final Faker FAKER = new Faker();

    private BookDataFactory() {
    }

    public static CreateBookRequest createBook() {

        return CreateBookRequest.builder()
                .title(FAKER.book().title())
                .author(FAKER.book().author())
                .genre(FAKER.book().genre())
                .price(
                        FAKER.number()
                                .randomDouble(2, 10, 100)
                )
                .build();
    }
}