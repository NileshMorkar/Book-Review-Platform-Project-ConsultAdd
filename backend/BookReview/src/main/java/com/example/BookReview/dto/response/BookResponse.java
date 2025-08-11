package com.example.BookReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private int id;

    private String title;

    private String summary;

    private String author;

    private String genre;

    private String isbn;

    private int publicationYear;

    private String coverImage;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime updatedAt;

    private double rating;

    private int likeCount;
}
