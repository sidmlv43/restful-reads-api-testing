package com.restfulReads.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private ExtentManager() {}

    private static class Holder {
        private static final ExtentReports INSTANCE = buildInstance();

        private static ExtentReports buildInstance() {
            ExtentSparkReporter reporter =
                    new ExtentSparkReporter("test-output/ExtentReport.html");
            ExtentReports reports = new ExtentReports();
            reports.attachReporter(reporter);
            reports.setSystemInfo("Framework", "Restful Reads API Automation");
            reports.setSystemInfo("Author", "Siddharth Malviya");
            return reports;
        }
    }

    public static ExtentReports getInstance() {
        return Holder.INSTANCE;
    }
}
