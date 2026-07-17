package com.restfulReads.base;

import com.restfulReads.config.RestAssuredConfig;
import com.restfulReads.listeners.ExtentTestListener;
import com.restfulReads.listeners.RetryTransformer;
import com.restfulReads.listeners.UserContextListener;

import com.restfulReads.services.BookService;
import com.restfulReads.services.CartService;
import com.restfulReads.session.UserPoolInitializer;
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
        System.out.println("BeforeSuite Executed");
        UserPoolInitializer.initialize();
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