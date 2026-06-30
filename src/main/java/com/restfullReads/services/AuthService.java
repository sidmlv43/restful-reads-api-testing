package com.restfullReads.services;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.constants.AuthEndPoints;
import com.restfullReads.models.AuthToken;
import com.restfullReads.models.LoginRequest;
import com.restfullReads.models.RegisterRequest;
import com.restfullReads.session.SessionManager;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthService {

    public String login(LoginRequest request) {

        AuthToken authToken =
                given()
                        .baseUri(ConfigManager.getBaseUrl())
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .post(AuthEndPoints.LOGIN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AuthToken.class);

        SessionManager.setToken(authToken.getToken());

        return authToken.getToken();
    }


    public String register(RegisterRequest request) {

        AuthToken authToken =
                given()
                        .baseUri(ConfigManager.getBaseUrl())
                        .body(request)
                        .when()
                        .post(AuthEndPoints.REGISTER)
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(AuthToken.class);

        SessionManager.setToken(authToken.getToken());



        return authToken.getToken();
    }
}