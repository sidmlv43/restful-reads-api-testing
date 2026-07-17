package com.restfulReads.listeners;

import com.restfulReads.annotations.UseUser;
import com.restfulReads.enums.UserType;
import com.restfulReads.session.SessionManager;
import com.restfulReads.session.User;
import com.restfulReads.session.UserPool;
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
        User user = UserPool.acquire(userType);

        SessionManager.use(user);

        System.out.printf(
                "ACQUIRED -> %s (%s)%n",
                user.getEmail(),
                user.getUserType()
        );

    }

    @Override
    public void afterInvocation(
            IInvokedMethod method,
            ITestResult testResult
    ) {

        if (!method.isTestMethod()) {
            return;
        }

        User currentUser =
                SessionManager.getCurrentUser();

        UserPool.release(currentUser);

        SessionManager.clear();
    }


}