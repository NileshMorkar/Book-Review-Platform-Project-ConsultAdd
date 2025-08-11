package com.example.BookReview.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotEmpty(message = "Book Title Is Required")
    private String title;

    @Column(length = 1000)
    @Length(max = 1000, message = "Summary cannot be greater 1000 characters")
    private String summary;

    @Column(nullable = false)
    @NotEmpty(message = "Book Author Is Required")
    private String author;

    @Column(nullable = false)
    @NotEmpty(message = "Book Genre Is Required")
    private String genre;

    @Column(unique = true, nullable = false)
    private String isbn;


    @Column(nullable = false)
    private int publicationYear;

    private String coverImage;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
