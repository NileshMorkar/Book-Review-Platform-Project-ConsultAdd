package com.example.BookReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookCommentResponse {
    private int commentId;
    private int bookId;
    private long userId;
    private String commentMsg;
    
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime updatedAt;
}
