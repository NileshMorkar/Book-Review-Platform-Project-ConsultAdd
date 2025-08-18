package com.example.BookReview.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class BookRatingRequest {
    @Max(value = 5, message = "Rating Must be Less Than 5")
    @Min(value = 0, message = "Rating Must be Greater Than 0")
    private Integer rating;
}
