package com.restfulReads.listeners;

import com.restfulReads.annotations.UseUser;
import com.restfulReads.enums.UserType;
import com.restfulReads.session.SessionManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

public class UserContextListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

        if (!method.isTestMethod()) {
            return;
        }

        Method testMethod = method.getTestMethod()
                .getConstructorOrMethod()
                .getMethod();

        UseUser annotation = testMethod.getAnnotation(UseUser.class);

        if (annotation == null){
            return;
        }

        UserType userType = annotation.value();
        SessionManager.use(userType);
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

        if (!method.isTestMethod()) {
            return;
        }

        SessionManager.clear();
    }


}