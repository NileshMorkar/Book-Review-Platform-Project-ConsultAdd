package com.example.BookReview.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String title;
    private String summary;
    private String author;
    private String genre;
    private String isbn;
    private int publicationYear;
    private String coverImage;
}
