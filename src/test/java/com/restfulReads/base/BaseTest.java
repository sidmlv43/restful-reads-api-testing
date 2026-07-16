package com.restfulReads.base;

import com.restfulReads.config.RestAssuredConfig;
import com.restfulReads.enums.UserType;
import com.restfulReads.listeners.ExtentTestListener;
import com.restfulReads.listeners.RetryAnalyzer;
import com.restfulReads.listeners.RetryTransformer;
import com.restfulReads.listeners.UserContextListener;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.services.AuthService;
import com.restfulReads.services.BookService;
import com.restfulReads.services.CartService;
import com.restfulReads.session.TokenManager;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;


@Listeners({
        UserContextListener.class,
        ExtentTestListener.class,
        RetryTransformer.class
})
public class BaseTest {
    protected CartService cartService;
    protected BookService bookService;

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

    @BeforeMethod
    public void initServices() {
        cartService = new CartService();
        bookService = new BookService();
    }
}