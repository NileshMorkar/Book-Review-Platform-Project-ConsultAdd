package com.example.BookReview.repository;

import com.example.BookReview.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {


    @Query(value = "select COUNT(*) from book_likes where book_id = :bookId", nativeQuery = true)
    int getBookLikesCountByBookId(@Param("bookId") int bookId);

    @Query(value = "select AVG(rating) from book_ratings where book_id = :bookId", nativeQuery = true)
    Optional<Double> getBookRatingByBookId(@Param("bookId") int bookId);

    @Query(
            value = "SELECT * FROM books " +
                    "WHERE title LIKE CONCAT('%', :searchString, '%') " +
                    "OR summary LIKE CONCAT('%', :searchString, '%') " +
                    "OR genre LIKE CONCAT('%', :searchString, '%') " +
                    "OR author LIKE CONCAT('%', :searchString, '%')",
            countQuery = "SELECT count(*) FROM books " +
                    "WHERE title LIKE CONCAT('%', :searchString, '%') " +
                    "OR summary LIKE CONCAT('%', :searchString, '%') " +
                    "OR genre LIKE CONCAT('%', :searchString, '%') " +
                    "OR author LIKE CONCAT('%', :searchString, '%')",
            nativeQuery = true
    )
    Page<Book> searchBook(@Param("searchString") String searchString, Pageable pageable);

    
}
