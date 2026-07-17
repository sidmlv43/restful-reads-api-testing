package com.restfulReads.tests;

import com.restfulReads.annotations.Author;
import com.restfulReads.annotations.UseUser;
import com.restfulReads.base.BaseTest;
import com.restfulReads.enums.UserType;
import com.restfulReads.models.requests.AddItemToCartRequest;
import com.restfulReads.models.responses.cart.Cart;
import com.restfulReads.query.BookQueryParams;
import com.restfulReads.services.CartService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.restfulReads.assertions.CartAssertions.assertCartTaxAndSubtotalCalculationsAccuracy;
import static org.hamcrest.Matchers.notNullValue;

public class UserJourneyTests extends BaseTest {



    @BeforeMethod
    public void initCartService() {
        cartService = new CartService();
    }

    @Test(description = "Customer can check his cart")
    @Author("Siddharth Malviya")
    @UseUser(UserType.CUSTOMER)
    public void testUserCart() {
        cartService
                .getCart()
                .then()
                .statusCode(200)
                .body(".", notNullValue());
    }




    @Test(description = "Test Customer can add items in cart")
    @Author("Siddharth Malviya")
    @UseUser(UserType.CUSTOMER)
    public void tetsCustomerCanAddItemsInCart() {
        BookQueryParams queryParams = BookQueryParams.builder()
                .filters(Map.of("price[lte]", "14")).limit(1).sort("price").build();
        List<String> bookIds = bookService.getBooks(queryParams)
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("results._id");


        bookIds.forEach(id ->
                cartService.addItemToCart(AddItemToCartRequest.builder().bookId(id).quantity(1).build()).then().statusCode(201)
                );

        Cart cart = cartService.getCart()
                .then()
                .statusCode(200)
                .body("cart._id", notNullValue())
                .body("cart.user", notNullValue())
                .extract().jsonPath().getObject("cart", Cart.class);

        assertCartTaxAndSubtotalCalculationsAccuracy(cart);

    }

}
