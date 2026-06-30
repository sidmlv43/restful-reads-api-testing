package com.restfullReads.services;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.session.SessionManager;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthService {

    private AuthService() {

    }


    public static String login(String email, String password) {
        Response response = given()
                .baseUri(ConfigManager.getBaseUrl())
                .body(Map.of("email", email, "password", password))
                .when()
                .post("/api/auth/login");

        response.then().statusCode(200);


        String token = response.jsonPath().getString("token");

        SessionManager.setToken(token);

        return token;
    }

}
