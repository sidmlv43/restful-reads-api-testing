package com.restfullReads.session;

import lombok.Getter;
import lombok.Setter;


public class SessionManager {
    @Setter
    @Getter
    private static final ThreadLocal<String> token = new ThreadLocal<>();

    private SessionManager() {

    }


    public static void setToken(String authToken) {
        token.set(authToken);
    }



    public static String getToken() {
        String value = token.get();
        if (value == null) {
            throw new RuntimeException("No token available for this thread. Please login first.");
        }
        return value;
    }



    public static void clear() {
        token.remove();
    }

}
