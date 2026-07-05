package com.restfullReads.tests;

import com.restfullReads.annotations.Author;
import com.restfullReads.annotations.UseUser;
import com.restfullReads.annotations.ZephyrTest;
import com.restfullReads.assertions.BookAssertion;
import com.restfullReads.base.BaseTest;
import com.restfullReads.data.BookDataFactory;
import com.restfullReads.enums.UserType;
import com.restfullReads.models.responses.Book;
import com.restfullReads.query.BookQueryParams;
import com.restfullReads.services.BookService;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertEquals;

import java.util.List;
import java.util.Map;

public class BookServiceTest extends BaseTest {

    private BookService bookService;
    private String createdBookId;


    @BeforeTest
    public void testSetup() {
        bookService = new BookService();
    }


    @Test(description = "Test Get all books by query")
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_101")
    @UseUser(UserType.ADMIN)
    public void getAllBooksByQueryTest() {
        BookQueryParams bookQueryParams = BookQueryParams.builder()
                .page(1)
                .limit(10)
                .author("Rick Riordan")
                .minRating(0)
                .sort("-createdAt")
                .build();


        Response res = bookService.getBooks(bookQueryParams);
        res.then()
                .statusCode(200);


    }


    @Test(description = "Test Price filter works properly")
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_102")
    public void testBookQueryParamGreaterThanAndEqualsToFilterWorks() {

        BookQueryParams queryParams = BookQueryParams.builder()
                .page(1)
                .limit(10)
                .filters(
                        Map.of("price[gte]", 10)
                ).build();



        Response res = bookService.getBooks(queryParams);
        List<Double> prices = res.jsonPath().getList("results.price", Double.class);
        BookAssertion.assertValueGreaterThanOrEqualsTo(prices, 10);


    }


    @Test(testName = "Test Price filter works properly")
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_103")
    public void testBookQueryParamLessThanAndEqualsToFilterWorks() {

        BookQueryParams queryParams = BookQueryParams.builder()
                .page(1)
                .limit(10)
                .filters(
                        Map.of("price[lte]", 10)
                ).build();



        Response res = bookService.getBooks(queryParams);
        List<Double> prices = res.jsonPath().getList("results.price", Double.class);
        BookAssertion.assertValueLessThanOrEqualsTo(prices, 10);


    }


    @Test(description = "Test Customer cannot add a new book")
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_104")
    @UseUser(UserType.CUSTOMER)
    public void testCustomerCannotAddBook() {
        bookService.createBook(BookDataFactory.createBook())
                .then()
                .statusCode(403);

    }

    @Test(description = "Test Admin can add a new book")
    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_105")
    @UseUser(UserType.ADMIN)
    public void testAdminCanCreateBook() {

        Book book = bookService.createBook(BookDataFactory.createBook())
            .then()
                .statusCode(201)
                .body("_id", notNullValue())
            .extract()
            .as(Book.class);

        Assert.assertFalse(book.getId().isEmpty(), "Id is expected not to be null or blank");

        this.createdBookId = book.getId();
    }

    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_106")
    @Test(
            description = "Test Admin can update the created book",
            dependsOnMethods = "testAdminCanCreateBook"
    )
    @UseUser(UserType.ADMIN)
    public void testAdminCanUpdateBookDetails() {
        Response updatedBookResponse = bookService.updateBook(createdBookId, Map.of("price", 44.44));
        updatedBookResponse.then()
                .body("price", equalTo(44.44F))
                .body("_id", notNullValue());

    }

    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_107")
    @Test(
            description = "Test Admin can delete the created book",
            dependsOnMethods = "testAdminCanUpdateBookDetails"
    )
    @UseUser(UserType.ADMIN)
    public void testAdminCanDeleteBook() {
        Response deletedBook = bookService.deleteBook(createdBookId);
        deletedBook.then()
                .statusCode(200)
               ;

        bookService.
                getBookById(createdBookId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Not found"))
                .body("details", nullValue());
    }


    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_108")
    @Test(
            description = "Test Admin can delete the created book",
            dependsOnMethods = "testAdminCanCreateBook"
    )
    @UseUser(UserType.CUSTOMER)
    public void testCustomerCannotDeleteABook() {
        Response deletedBook = bookService.deleteBook(createdBookId);
        deletedBook.then()
                .statusCode(403)
                .body("message", containsString("Forbidden"))
                .time(lessThan(140L))
        ;
    }


    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_109")
    @Test(description = "Anonymous User should not create a new book.")
    public void anonymousUserCannotAddBookTest() {
        bookService.createBook(BookDataFactory.createBook())
                .then()
                .statusCode(401)
                .body("message", containsString("denied"))
                .time(lessThan(200L));
    }


}
