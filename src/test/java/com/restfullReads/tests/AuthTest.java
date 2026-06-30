package com.restfullReads.tests;

import com.restfullReads.base.BaseTest;
import com.restfullReads.models.LoginRequest;
import com.restfullReads.models.RegisterRequest;
import com.restfullReads.services.AuthService;
import com.restfullReads.session.SessionManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.restfullReads.assertions.AuthAssertions.assertTokenValid;

public class AuthTest extends BaseTest {

    private AuthService authService;
    private RegisterRequest registerRequest;

    @BeforeMethod
    public void initialize() {
        authService = new AuthService();
    }


    @Test(testName = "test user successful login")
    public void testSuccessfulLogin() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("riya@test.com")
                .password("pass1234")
                .build();
        authService.login(loginRequest);

        assertTokenValid(SessionManager.getToken());

    }
}
