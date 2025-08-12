package com.example.BookReview.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "book_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"bookId", "userId"})
)
public class BookLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msg;


    @ManyToOne(optional = false)
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private User user;


    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
