package com.restfulReads.models.responses.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cart {

    @JsonProperty("_id")
    private String id;

    private String user;

    private List<CartItem> items;

    private CartSummary summary;
}