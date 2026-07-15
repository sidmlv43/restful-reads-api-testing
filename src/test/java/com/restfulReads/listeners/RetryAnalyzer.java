package com.restfulReads.listeners;

import com.restfulReads.config.ConfigManager;
import com.restfulReads.reporting.ExtentTestManager;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private final static int maxRetryCount = ConfigManager.getRetryCount();
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            ExtentTestManager.getTest()
                    .warning("Retrying Test, Attempt: " + retryCount);

            return true;
        }
        return false;
    }

}
