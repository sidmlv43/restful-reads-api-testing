package com.restfulReads.services;

import com.restfulReads.constants.AuthEndPoints;
import com.restfulReads.models.responses.AuthToken;
import com.restfulReads.models.requests.LoginRequest;
import com.restfulReads.models.requests.RegisterRequest;
import com.restfulReads.services.base.BaseService;

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