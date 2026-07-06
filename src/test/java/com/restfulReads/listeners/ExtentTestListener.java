package com.restfulReads.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.restfulReads.annotations.Author;
import com.restfulReads.annotations.ZephyrTest;
import com.restfulReads.reporting.ExtentManager;
import com.restfulReads.reporting.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {

        String testName = result.getMethod().getDescription();

        if (testName == null || testName.isEmpty()) {
            testName = result.getMethod().getMethodName();
        }

        ExtentTest test = ExtentManager.getInstance()
                .createTest(testName);

        ExtentTestManager.setTest(test);

        Method method = result.getMethod()
                .getConstructorOrMethod()
                .getMethod();

        Author author = method.getAnnotation(Author.class);

        if (author != null) {
            test.assignAuthor(author.value());
        }

        ZephyrTest zephyrTest =
                method.getAnnotation(ZephyrTest.class);

        if (zephyrTest != null) {
            test.info(
                    "Zephyr Test Case: "
                            + zephyrTest.value()
            );
        }

        String[] groups = result.getMethod().getGroups();

        if (groups.length > 0) {

            Arrays.stream(groups)
                    .forEach(test::assignCategory);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        ExtentTestManager.getTest()
                .pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        ExtentTestManager.getTest()
                .fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        ExtentTestManager.getTest()
                .skip(
                        result.getThrowable() != null
                                ? result.getThrowable()
                                : new RuntimeException(
                                "Test Skipped"
                        )
                );
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

        ExtentTestManager.getTest()
                .fail(
                        "Test failed due to timeout"
                );

        if (result.getThrowable() != null) {
            ExtentTestManager.getTest()
                    .fail(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        ExtentManager.getInstance()
                .flush();

        ExtentTestManager.remove();
    }
}