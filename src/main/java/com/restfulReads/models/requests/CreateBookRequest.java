package com.restfulReads.models.requests;

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

    private String description;

    private String author;

    private String genre;

    private double price;

}