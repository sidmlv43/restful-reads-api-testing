package com.restfulReads.models.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class AddItemToCartRequest {
    private String bookId;
    private int quantity;
}
