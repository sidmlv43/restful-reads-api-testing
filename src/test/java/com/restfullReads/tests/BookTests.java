package com.restfullReads.tests;

import com.restfullReads.annotations.UseUser;
import com.restfullReads.base.BaseTest;
import com.restfullReads.enums.UserType;
import com.restfullReads.services.BookService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BookTests extends BaseTest {

    private BookService bookService;

    @BeforeMethod
    public void setBookService() {
        bookService = new BookService();
    }

    @Test(description = "Test successful creation of book")
    @UseUser(UserType.ADMIN)
    public void testSuccessfulCreationOfBook() {


//        bookService.createBook()
    }
}
