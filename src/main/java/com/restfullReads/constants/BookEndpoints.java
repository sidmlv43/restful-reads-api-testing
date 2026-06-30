package com.restfullReads.constants;


public class BookEndpoints {

    private BookEndpoints() {}

    public static final String BASE = "/api/books";

    public static String getBookById(String id) {
        return BASE + "/" + id;
    }

    public static final String RATE_BOOK = "/api/books/%s/rate";
}