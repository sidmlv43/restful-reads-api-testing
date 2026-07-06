package com.restfulReads.services;

import com.restfulReads.constants.BookEndpoints;
import com.restfulReads.query.BookQueryParams;
import com.restfulReads.models.requests.CreateBookRequest;
import com.restfulReads.services.base.BaseService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class BookService extends BaseService {


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
                .body(bookRequest)
                .when()
                .post(BookEndpoints.BASE);
    }

    public Response deleteBook(String bookId) {

        return request()
                .when()
                .delete(BookEndpoints.getBookById(bookId));
    }

    public Response updateBook(String id, Object updatePatch) {
        return request()
                .when()
                .body(updatePatch)
                .patch(BookEndpoints.getBookById(id));
    }
}