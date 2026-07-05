package com.restfullReads.services.base;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.session.SessionManager;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseService {
    protected RequestSpecification request() {
        RequestSpecification request = given()
                .baseUri(ConfigManager.getBaseUrl())
                .contentType(ContentType.JSON);


        String authToken = SessionManager.getToken();
        if (authToken != null) {
            request.header("Authorization", "Bearer " + authToken);
        }


        return request;
    }
}
