package com.restfullReads.base;

import com.restfullReads.config.RestAssuredConfig;
import com.restfullReads.enums.UserType;
import com.restfullReads.listeners.UserContextListener;
import com.restfullReads.models.LoginRequest;
import com.restfullReads.services.AuthService;
import com.restfullReads.session.TokenManager;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;


@Listeners({
        UserContextListener.class
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