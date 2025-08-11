package com.example.BookReview.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCommentRequest {
    private int bookId;
    @NotBlank
    private String commentMsg;
}
