package com.restfulReads.testgroups;

public final class TestGroups {

    private TestGroups() {

    }

    // Execution Groups
    public static final String API_SMOKE = "smoke";
    public static final String API_REGRESSION = "regression";
    public static final String API_SEV1 = "critical";

    // Functional Areas
    public static final String BOOKS = "books";
    public static final String CART = "cart";
    public static final String ORDERS = "orders";
    public static final String ADDRESSES = "addresses";
    public static final String USERS = "users";
    public static final String RATINGS = "ratings";

    // Security / Auth
    public static final String AUTH = "auth";

}