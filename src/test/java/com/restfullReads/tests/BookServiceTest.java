package com.restfullReads.tests;

import com.restfullReads.assertions.BookAssertion;
import com.restfullReads.base.BaseTest;
import com.restfullReads.models.BookQueryParams;
import com.restfullReads.services.BookService;
import io.restassured.response.Response;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class BookServiceTest extends BaseTest {

    private BookService bookService;

    @BeforeTest
    public void testSetup() {
        bookService = new BookService();
    }


    @Test
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



}
