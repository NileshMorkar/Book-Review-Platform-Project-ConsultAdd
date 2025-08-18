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

    private long likeCount;

    private boolean isLiked;

    public BookResponse(
            int id,
            String title,
            String summary,
            String author,
            String genre,
            String isbn,
            int publicationYear,
            String coverImage,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long likeCount,
            Boolean isLiked,
            Double rating
    ) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.coverImage = coverImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rating = rating;
        this.likeCount = likeCount != null ? likeCount.intValue() : 0;
        this.isLiked = isLiked;
    }

}
