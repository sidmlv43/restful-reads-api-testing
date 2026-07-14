package com.restfulReads.dataproviders;

import com.restfulReads.data.BookDataFactory;
import org.testng.annotations.DataProvider;

public class BookServiceTestDataProvider {

    @DataProvider(name = "bookDataProvider")
    public Object[][] bookDataProvider() {

        Object[][] data = new Object[5][1];

        for (int i = 0; i < 5; i++) {
            data[i][0] = BookDataFactory.createBook();
        }

        return data;
    }
}