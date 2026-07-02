package com.restfullReads.session;

import com.restfullReads.enums.UserType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenManager {

    private static final Map<UserType, String> TOKEN_STORE = new ConcurrentHashMap<>();

    private TokenManager() {

    }

    public static void register(UserType userType, String token) {
        TOKEN_STORE.put(userType, token);
    }

    public static String get(UserType userType) {
        String token = TOKEN_STORE.get(userType);

        if (token == null) {
            throw new RuntimeException(
              String.format("no token found for %s", userType)
            );
        }
        return token;
    }


    public static boolean contains (UserType type) {
        return TOKEN_STORE.containsKey(type);
    }

    public static void clear() {
        TOKEN_STORE.clear();
    }

}
