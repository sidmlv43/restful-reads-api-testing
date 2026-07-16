package com.restfulReads.services;

import com.restfulReads.constants.CartEndpoints;
import com.restfulReads.models.requests.AddItemToCartRequest;
import com.restfulReads.services.base.BaseService;
import io.restassured.response.Response;

import java.util.List;

import static com.restfulReads.constants.CartEndpoints.ADD_ITEM_TO_CART;

public class CartService extends BaseService {

    public Response getCart() {
        return request()
                .get(CartEndpoints.GET_CART);
    }

    public Response addItemToCart(AddItemToCartRequest cartItem) {
        return request()
                .when()
                .body(cartItem)
                .post(ADD_ITEM_TO_CART);
    }


}
