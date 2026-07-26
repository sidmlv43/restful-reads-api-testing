package com.restfulReads.config;

import io.restassured.RestAssured;

public class RestAssuredConfig {

    private RestAssuredConfig() {

    }

    public static void enableLogging() {
        RestAssured.filters(new SafeLoggingFilter());
    }
}
