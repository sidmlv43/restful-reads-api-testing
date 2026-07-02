package com.restfullReads.session;

import com.restfullReads.enums.UserType;
import lombok.Getter;
import lombok.Setter;

public final class SessionManager {

    private static final ThreadLocal<String> ACTIVE_TOKEN =
            new ThreadLocal<>();

    private SessionManager() {}

    public static void use(UserType userType) {

        String token = TokenManager.get(userType);

        ACTIVE_TOKEN.set(token);
    }

    public static String getToken() {

        String token = ACTIVE_TOKEN.get();

        if (token == null || token.isEmpty()) {
            throw new RuntimeException(
                    "No active session found. Call SessionManager.use() first."
            );
        }

        return token;
    }

    public static void clear() {
        ACTIVE_TOKEN.remove();
    }
}