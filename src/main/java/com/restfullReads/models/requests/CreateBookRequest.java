package com.restfullReads.models.requests;

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

    private String genre;

    private Double price;



}