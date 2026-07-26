package com.restfulReads.services;

import com.restfulReads.constants.BookEndpoints;
import com.restfulReads.query.BookQueryParams;
import com.restfulReads.models.requests.CreateBookRequest;
import com.restfulReads.services.base.BaseService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.List;


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


    public Response createBook(CreateBookRequest request, List<File> images) {

        RequestSpecification requestSpecification =
                request()
                        .contentType(ContentType.MULTIPART)
                        .multiPart(
                                "title",
                                request.getTitle()
                        )
                        .multiPart(
                                "description",
                                request.getDescription()
                        )
                        .multiPart(
                                "author",
                                request.getAuthor()
                        )
                        .multiPart(
                                "genre",
                                request.getGenre()
                        )
                        .multiPart(
                                "price",
                                String.valueOf(
                                        request.getPrice()
                                )
                        );

        for (File image : images) {

            requestSpecification.multiPart(
                    "images",
                    image,
                    "image/jpeg"
            );
        }

        return requestSpecification
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