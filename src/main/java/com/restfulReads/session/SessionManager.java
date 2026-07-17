package com.restfulReads.session;

import com.restfulReads.enums.UserType;

public final class SessionManager {

    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();


    private SessionManager() {}


    public static void use(User user) {
        CURRENT_USER.set(user);
    }

    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static String getToken() {
        User currentUser = CURRENT_USER.get();
        return currentUser == null ? null : currentUser.getToken();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}