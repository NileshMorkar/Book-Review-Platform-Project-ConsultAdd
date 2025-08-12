package com.example.BookReview.repository;

import com.example.BookReview.models.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLikeRepository extends JpaRepository<BookLike, Long> {
    void deleteAllByBookId(int bookId);

}
