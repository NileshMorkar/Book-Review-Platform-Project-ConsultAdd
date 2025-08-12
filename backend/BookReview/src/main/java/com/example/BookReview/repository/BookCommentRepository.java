package com.example.BookReview.repository;

import com.example.BookReview.models.BookComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Integer> {

    Page<BookComment> findAllByBook_Id(int id, Pageable pageable);

    void deleteAllByBookId(int bookId);

    Optional<BookComment> findByUserIdAndBookId(long userId, int bookId);

}
