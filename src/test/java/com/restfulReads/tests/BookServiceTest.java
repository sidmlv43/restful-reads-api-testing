package com.restfulReads.tests;

import com.restfulReads.annotations.Author;
import com.restfulReads.annotations.UseUser;
import com.restfulReads.annotations.ZephyrTest;
import com.restfulReads.assertions.BookAssertion;
import com.restfulReads.base.BaseTest;
import com.restfulReads.data.BookDataFactory;
import com.restfulReads.data.FileDataFactory;
import com.restfulReads.dataproviders.BookServiceTestDataProvider;
import com.restfulReads.enums.UserType;
import com.restfulReads.models.requests.CreateBookRequest;
import com.restfulReads.models.responses.Book;
import com.restfulReads.query.BookQueryParams;
import com.restfulReads.services.BookService;
import com.restfulReads.testgroups.TestGroups;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.List;
import java.util.Map;

public class BookServiceTest extends BaseTest {

    private BookService bookService;

    @BeforeMethod(alwaysRun = true)
    public void testSetup() {
        bookService = new BookService();
    }


    @Test(
            testName = "Test Get all books by query",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SMOKE
            }
    )
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
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/book-shema.json"));


    }


    @Test(
            description = "Test Price filter works properly",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SEV1,
                    TestGroups.API_REGRESSION
            }
    )
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
        res.
                then().
                statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/book-shema.json"));
        List<Double> prices = res.jsonPath().getList("results.price", Double.class);
        BookAssertion.assertValueGreaterThanOrEqualsTo(prices, 10);


    }


    @Test(
            testName = "Test Price filter works properly",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_REGRESSION,
                    TestGroups.API_SEV1,
                    TestGroups.API_SMOKE
            }

    )
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_103")
    public void testBookQueryParamLessThanAndEqualsToFilterWorks() {

        BookQueryParams queryParams = BookQueryParams.builder()
                .page(1)
                .limit(10)
                .filters(
                        Map.of("price[lte]", 400)
                ).build();



        Response res = bookService.getBooks(queryParams);
        List<Double> prices = res.jsonPath().getList("results.price", Double.class);
        BookAssertion.assertValueLessThanOrEqualsTo(prices, 400);


    }


    @Test(
            testName = "Test Customer cannot add a new book",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SMOKE,
                    TestGroups.API_REGRESSION
            }
    )
    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_104")
    @UseUser(UserType.CUSTOMER)
    public void testCustomerCannotAddBook() {
        bookService.createBook(BookDataFactory.createBook(), FileDataFactory.getBookImages(2))
                .then()
                .statusCode(403)
                .body(containsString("Forbidden"))
                .body(containsString("does not have required permissions"))
                .body(matchesJsonSchemaInClasspath("schemas/unauthorized-access-schema.json"));

    }

    @Test(
            testName = "Test Admin can add a new book",
            dataProviderClass = BookServiceTestDataProvider.class,
            dataProvider = "bookDataProvider",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_REGRESSION,
                    TestGroups.API_SMOKE
            }
    )
    @Author("Riya Malviya")
    @ZephyrTest("BOOKS_105")
    @UseUser(UserType.ADMIN)
    public void testAdminCanCreateBook(
            CreateBookRequest request
    ) {

        Book book = bookService.createBook(request, FileDataFactory.getBookImages(4))
                .then()
                .statusCode(201)
                .body("_id", notNullValue())
                .extract()
                .as(Book.class);

        Assert.assertFalse(
                book.getId().isEmpty(),
                "Id is expected not to be null or blank"
        );

    }

    @Test(
            testName = "Test Created Book can be fetched",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_REGRESSION,
                    TestGroups.API_SMOKE
            }
    )
    @Author("Siddharth Malviya")
    @ZephyrTest("BOOKS_110")
    public void testGetBookById() {

        String id = bookService.getBooks(BookQueryParams.builder().limit(1).build()).jsonPath().get("results[0]._id");
        System.out.println("id: " + id);

        bookService.getBookById(id)
                .then()
                .statusCode(200)
                .body("data._id", notNullValue())
                .body(matchesJsonSchemaInClasspath("schemas/book_by_id_schema.json"));
    }

    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_106")
    @Test(
            testName = "Test Admin can update the created book",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SEV1,
                    TestGroups.API_SMOKE
            }
    )
    @UseUser(UserType.ADMIN)
    public void testAdminCanUpdateBookDetails() {
        String id = bookService.getBooks(BookQueryParams.builder().limit(4).build()).jsonPath().get("results[3]._id");

        Response updatedBookResponse = bookService.updateBook(id, Map.of("price", 44.44));
        updatedBookResponse.then()
                .body("price", equalTo(44.44F))
                .body("_id", notNullValue());

    }

    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_107")
    @Test(
            testName = "Test Admin can delete the created book",

            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SEV1,
                    TestGroups.API_REGRESSION
            }

    )
    @UseUser(UserType.ADMIN)
    public void testAdminCanDeleteBook() {
        String id = bookService.getBooks(BookQueryParams.builder().limit(4).build()).jsonPath().get("results[3]._id");

        Response deletedBook = bookService.deleteBook(id);
        deletedBook.then()
                .statusCode(200);

        bookService.
                getBookById(id)
                .then()
                .statusCode(404)
                .body("message", equalTo("Not found"))
                .body("details", nullValue());

    }


    @Author("Riya Malviya")
    @ZephyrTest(value = "BOOKS_108")
    @Test(
            testName = "Test Admin can delete the created book",
            groups = {
                    TestGroups.API_SEV1,
                    TestGroups.API_REGRESSION,
                    TestGroups.BOOKS,

            }
    )
    @UseUser(UserType.CUSTOMER)
    public void testCustomerCannotDeleteABook() {

        String id = bookService.getBooks(BookQueryParams.builder().limit(4).build()).jsonPath().get("results[3]._id");
        Response deletedBook = bookService.deleteBook(id);
        deletedBook.then()
                .statusCode(403)
                .body("message", containsString("Forbidden"))
                .time(lessThan(140L))
        ;
    }


    @Author("Siddharth Malviya")
    @ZephyrTest(value = "BOOKS_109")
    @Test(
            testName = "Anonymous User should not create a new book.",
            groups = {
                    TestGroups.BOOKS,
                    TestGroups.API_SMOKE,
                    TestGroups.API_REGRESSION
            }
    )
    public void anonymousUserCannotAddBookTest() {
        bookService.createBook(BookDataFactory.createBook(), FileDataFactory.getBookImages(5))
                .then()
                .statusCode(401)
                .body("message", containsString("denied"))
                .time(lessThan(200L));
    }


}
