package com.restfulReads.assertions;

import com.restfulReads.models.responses.cart.Cart;
import org.testng.Assert;


public class CartAssertions {
    public static void assertCartTaxAndSubtotalCalculationsAccuracy(
            Cart cart
    ) {

        double expectedSubtotal =
                cart.getItems()
                        .stream()
                        .mapToDouble(
                                item ->
                                        item.getPrice()
                                                * item.getQuantity()
                        )
                        .sum();

        Assert.assertEquals(
                cart.getSummary().getSubtotal(),
                expectedSubtotal,
                0.01,
                "Cart subtotal calculation is incorrect"
        );
    }
}
