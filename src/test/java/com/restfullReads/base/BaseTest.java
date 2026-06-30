package com.restfullReads.base;

import com.restfullReads.config.RestAssuredConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class BaseTest {

    @BeforeTest
    public void setup() {
        RestAssuredConfig.enableLogging();
    }

}
