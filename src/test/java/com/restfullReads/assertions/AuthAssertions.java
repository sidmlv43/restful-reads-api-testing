package com.restfullReads.assertions;

import org.testng.Assert;

public class AuthAssertions {

    public static void assertTokenValid(String token) {
        Assert.assertNotNull(token, "Token should not be null");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");
    }

    public static void assertTokenIsValidJWT(String token) {
        int len = token.split("\\.").length;
        Assert.assertEquals(len, 3, "JWT must contain header, payload and signature");

    }
}
