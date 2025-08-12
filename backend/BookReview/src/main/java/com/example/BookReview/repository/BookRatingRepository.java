package com.example.BookReview.repository;

import com.example.BookReview.models.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {
    void deleteAllByBookId(int bookId);

}
