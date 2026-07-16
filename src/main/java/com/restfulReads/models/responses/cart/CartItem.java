package com.restfulReads.models.responses.cart;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.restfulReads.models.responses.Book;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {

    @JsonProperty("_id")
    private String id;

    private Book book;

    private int quantity;

    private double price;

    private String createdAt;

    private String updatedAt;
}