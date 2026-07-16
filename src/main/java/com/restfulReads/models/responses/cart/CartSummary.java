package com.restfulReads.models.responses.cart;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartSummary {

    private double subtotal;

    private double tax;

    private double total;

    private double taxRate;
}