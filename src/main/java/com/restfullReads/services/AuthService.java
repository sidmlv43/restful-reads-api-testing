package com.restfullReads.services;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.constants.AuthEndPoints;
import com.restfullReads.models.responses.AuthToken;
import com.restfullReads.models.requests.LoginRequest;
import com.restfullReads.models.requests.RegisterRequest;
import com.restfullReads.services.base.BaseService;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class AuthService extends BaseService {

    public String login(LoginRequest request) {

        AuthToken authToken =
                request()
                        .body(request)
                        .when()
                        .post(AuthEndPoints.LOGIN)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AuthToken.class);


        return authToken.getToken();
    }


    public String register(RegisterRequest request) {

        AuthToken authToken =
                request()
                        .body(request)
                        .when()
                        .post(AuthEndPoints.REGISTER)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(AuthToken.class);

        return authToken.getToken();
    }
}