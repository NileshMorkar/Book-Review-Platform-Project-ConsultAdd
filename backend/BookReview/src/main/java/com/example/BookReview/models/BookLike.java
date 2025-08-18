package com.example.BookReview.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "book_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"bookId", "userId"})
)
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class BookLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
