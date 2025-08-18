package com.example.BookReview.repository;

import com.example.BookReview.models.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Long> {
    void deleteAllByBookId(int bookId);


    Optional<BookRating> findByUser_IdAndBook_Id(long userId, int bokId);


}
