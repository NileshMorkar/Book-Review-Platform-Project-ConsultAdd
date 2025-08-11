package com.example.BookReview.repository;

import com.example.BookReview.models.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentRepository extends JpaRepository<BookComment, Integer> {
}
