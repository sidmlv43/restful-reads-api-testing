package com.restfullReads.base;

import com.restfullReads.config.RestAssuredConfig;
import com.restfullReads.session.SessionManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    @BeforeTest
    public void setup() {
        RestAssuredConfig.enableLogging();
    }

    @AfterMethod
    public void teardown() {
        // clears the auth tokens
        SessionManager.clear();
    }

}
