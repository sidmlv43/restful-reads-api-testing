package com.restfullReads.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    private String title;

    private String author;

    private Double price;

    private Integer stock;
}