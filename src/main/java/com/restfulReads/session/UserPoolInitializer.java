package com.restfulReads.session;

import com.restfulReads.enums.UserType;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.services.AuthService;

import java.util.ArrayList;
import java.util.List;

public class UserPoolInitializer {

    private UserPoolInitializer () {

    }

    public static void initialize() {
        AuthService authService = new AuthService();
        List<UserCredential> userCredentials = loadCredentials();
        userCredentials.forEach(cred -> {
            String token = authService.login(
                    LoginRequest.builder()
                            .email(cred.getEmail())
                            .password(cred.getPassword())
                            .build()
            );
            UserPool.register(
                    User
                            .builder()
                            .userType(cred.getUserType())
                            .email(cred.getEmail())
                            .password(cred.getPassword())
                            .token(token)
                            .build());
        });
    }

    private static List<UserCredential> loadCredentials() {

        List<UserCredential> credentials =
                new ArrayList<>();

        credentials.add(
                UserCredential.builder()
                        .email("admin@example.com")
                        .password("adminpass")
                        .userType(UserType.ADMIN)
                        .build()
        );

        for (int i = 2; i <= 20; i++) {

            credentials.add(
                    UserCredential.builder()
                            .email(
                                    "admin" + i + "@example.com"
                            )
                            .password("adminpass")
                            .userType(UserType.ADMIN)
                            .build()
            );
        }

        for (int i = 1; i <= 20; i++) {

            credentials.add(
                    UserCredential.builder()
                            .email(
                                    "cust" + i + "@example.com"
                            )
                            .password("custpass")
                            .userType(UserType.CUSTOMER)
                            .build()
            );
        }

        return credentials;
    }

}
