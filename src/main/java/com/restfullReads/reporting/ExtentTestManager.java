package com.restfullReads.reporting;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();


    private ExtentTestManager() {

    }

    public static void setTest(ExtentTest test) {
        TEST.set(test);
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void remove() {
        TEST.remove();
    }
}
