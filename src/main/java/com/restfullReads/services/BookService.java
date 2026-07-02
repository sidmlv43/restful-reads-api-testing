package com.restfullReads.services;

import com.restfullReads.config.ConfigManager;
import com.restfullReads.constants.BookEndpoints;
import com.restfullReads.models.BookQueryParams;
import com.restfullReads.models.CreateBookRequest;
import com.restfullReads.session.SessionManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BookService {

    private RequestSpecification request() {

        RequestSpecification request = given()
                .baseUri(ConfigManager.getBaseUrl());

        String token = SessionManager.getToken();

        if (token != null) {
            request.header(
                    "Authorization",
                    "Bearer " + token
            );
        }

        return request;
    }

    public Response getBooks(BookQueryParams queryParams) {

        RequestSpecification request = request();

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

        return request()
                .when()
                .get(BookEndpoints.getBookById(bookId));
    }

    public Response createBook(CreateBookRequest bookRequest) {

        return request()
                .contentType(ContentType.JSON)
                .body(bookRequest)
                .when()
                .post(BookEndpoints.BASE);
    }

    public Response deleteBook(String bookId) {

        return request()
                .when()
                .delete(BookEndpoints.getBookById(bookId));
    }
}