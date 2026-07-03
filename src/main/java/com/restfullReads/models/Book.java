package com.restfullReads.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Book {

    @JsonProperty("_id")
    private String id;

    private String title;

    private String author;

    private String genre;

    private Double price;

    private Integer ratingsCount;

    private Double averageRating;

    private String createdAt;

    private String updatedAt;
}