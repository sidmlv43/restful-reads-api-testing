package com.restfullReads.listeners;

import com.restfullReads.annotations.UseUser;
import com.restfullReads.enums.UserType;
import com.restfullReads.session.SessionManager;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.lang.reflect.Method;

public class UserContextListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

        if(!method.isTestMethod()) {
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
        SessionManager.clear();
    }


}