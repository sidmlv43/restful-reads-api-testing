package com.restfulReads.constants;


public class BookEndpoints {

    private BookEndpoints() {}

    public static final String BASE = "/api/books";

    public static String getBookById(String id) {
        return BASE + "/" + id;
    }

    public static String rateBook(String id) {
        return BASE + "/" + id + "/rate";
    }}