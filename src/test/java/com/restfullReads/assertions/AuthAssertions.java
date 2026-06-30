package com.restfullReads.assertions;

import org.testng.Assert;

public class AuthAssertions {

    public static void assertTokenValid(String token) {
        Assert.assertNotNull(token, "Token should not be null");
        Assert.assertFalse(token.isEmpty(), "Token should not be empty");
    }
}
