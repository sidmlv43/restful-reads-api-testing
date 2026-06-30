package com.restfullReads.services;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.constants.BookEndpoints;
import com.restfullReads.models.BookQueryParams;
import com.restfullReads.session.SessionManager;

import io.restassured.response.Response;
import lombok.var;

import static io.restassured.RestAssured.given;

public class BookService {

    public Response getBooks(BookQueryParams queryParams) {

        var request = given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Authorization", "Bearer " + SessionManager.getToken());

        if (queryParams != null) {
            request.queryParams(queryParams.toMap());
        }

        return request
                .when()
                .get(BookEndpoints.BASE);
    }

    public Response getBooks() {
        return getBooks(null);
    }

    public Response getBookById(String bookId) {

        return given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .when()
                .get(BookEndpoints.getBookById(bookId));
    }

    public Response createBook(Object bookRequest) {

        return given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .body(bookRequest)
                .when()
                .post(BookEndpoints.BASE);
    }

    public Response deleteBook(String bookId) {

        return given()
                .baseUri(ConfigManager.getBaseUrl())
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .when()
                .delete(BookEndpoints.getBookById(bookId));
    }
}