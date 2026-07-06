package com.restfulReads.base;

import com.restfulReads.config.RestAssuredConfig;
import com.restfulReads.enums.UserType;
import com.restfulReads.listeners.ExtentTestListener;
import com.restfulReads.listeners.UserContextListener;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.services.AuthService;
import com.restfulReads.session.TokenManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;


@Listeners({
        UserContextListener.class,
        ExtentTestListener.class
})
public class BaseTest {

    @BeforeSuite
    public void initializeUsers() {

        AuthService authService = new AuthService();

        String adminToken = authService.login(
                LoginRequest.builder()
                        .email("admin@example.com")
                        .password("adminpass")
                        .build()
        );

        TokenManager.register(UserType.ADMIN, adminToken);

        String customerToken = authService.login(
                LoginRequest.builder()
                        .email("cust1@example.com")
                        .password("custpass")
                        .build()
        );

        TokenManager.register(UserType.CUSTOMER, customerToken);
    }

    @BeforeTest
    public void setup() {
        RestAssuredConfig.enableLogging();
    }
}