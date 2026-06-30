package com.restfullReads.session;

import lombok.Getter;
import lombok.Setter;


public class SessionManager {
    @Setter
    @Getter
    private static String token;

    private SessionManager() {

    }

    public static void clear() {
        token = null;
    }

}
