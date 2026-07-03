package com.restfullReads.tests;

import com.restfullReads.annotations.Author;
import com.restfullReads.annotations.ZephyrTest;
import com.restfullReads.base.BaseTest;
import com.restfullReads.models.LoginRequest;
import com.restfullReads.services.AuthService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.restfullReads.assertions.AuthAssertions.assertTokenIsValidJWT;
import static com.restfullReads.assertions.AuthAssertions.assertTokenValid;

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
                .email("riya@test.com")
                .password("pass1234")
                .build();
        String token = authService.login(loginRequest);

        assertTokenValid(token);
        assertTokenIsValidJWT(token);

    }
}
