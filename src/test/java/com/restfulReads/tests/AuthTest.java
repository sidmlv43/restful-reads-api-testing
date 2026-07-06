package com.restfulReads.tests;

import com.restfulReads.annotations.Author;
import com.restfulReads.annotations.ZephyrTest;
import com.restfulReads.base.BaseTest;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.services.AuthService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.restfulReads.assertions.AuthAssertions.assertTokenIsValidJWT;
import static com.restfulReads.assertions.AuthAssertions.assertTokenValid;

public class AuthTest extends BaseTest {

    private AuthService authService;

    @BeforeMethod
    public void initialize() {
        authService = new AuthService();
    }


    @Author("Riya Malviya")
    @ZephyrTest(value = "AUTH_101")
    @Test(testName = "test user successful login")
    public void testSuccessfulLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("cust1@example.com")
                .password("custpass")
                .build();
        String token = authService.login(loginRequest);

        assertTokenValid(token);
        assertTokenIsValidJWT(token);

    }
}
