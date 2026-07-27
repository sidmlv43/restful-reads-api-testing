package com.restfulReads.tests;

import com.restfulReads.annotations.Author;
import com.restfulReads.annotations.ZephyrTest;
import com.restfulReads.base.BaseTest;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.services.AuthService;
import com.restfulReads.testgroups.TestGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.restfulReads.assertions.AuthAssertions.assertTokenIsValidJWT;
import static com.restfulReads.assertions.AuthAssertions.assertTokenValid;

public class AuthTest extends BaseTest {

    private AuthService authService;

    @BeforeMethod(alwaysRun = true)
    public void initialize() {
        authService = new AuthService();
    }


    @Author("Riya Malviya")
    @ZephyrTest(value = "AUTH_101")
    @Test(
            testName = "test user successful login",
            groups = {
                    TestGroups.AUTH,
                    TestGroups.API_SEV1,
                    TestGroups.API_REGRESSION,
                    TestGroups.API_SMOKE
            }
    )
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
