package com.restfullReads.tests;

import com.restfullReads.annotations.UseUser;
import com.restfullReads.assertions.BookAssertion;
import com.restfullReads.base.BaseTest;
import com.restfullReads.data.BookDataFactory;
import com.restfullReads.enums.UserType;
import com.restfullReads.models.BookQueryParams;
import com.restfullReads.services.BookService;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

public class BookServiceTest extends BaseTest {

    private BookService bookService;
    private String createdBookId;


    @BeforeTest
    public void testSetup() {
        bookService = new BookService();
    }


    @Test
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


    @Test(testName = "Test Price filter works properly")
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
    @UseUser(UserType.CUSTOMER)
    public void testCustomerCannotAddBook() {
        bookService.createBook(BookDataFactory.createBook())
                .then()
                .statusCode(403);

    }

    @Test(description = "Test Admin can add a new book")
    @UseUser(UserType.ADMIN)
    public void testAdminCanCreateBook() {
        Response bookResponse = bookService.createBook(BookDataFactory.createBook());
        bookResponse.then()
                .statusCode(201)
                .body("_id", notNullValue())
                .body("$", hasKey("author"))
                .body("price", instanceOf(Float.class));
        this.createdBookId = bookResponse.body().jsonPath().get("_id");

        bookService.getBookById(createdBookId)
                .then().body("data._id", equalTo(createdBookId));

        System.out.println(this.createdBookId);
    }

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

}
