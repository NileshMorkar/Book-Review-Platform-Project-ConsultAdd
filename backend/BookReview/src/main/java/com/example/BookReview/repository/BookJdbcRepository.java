package com.example.BookReview.repository;

import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.repository.rowMapper.BookResponseRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BookJdbcRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<BookResponse> getAllBooks(Long userId, int page, int size) {
        String sql = """
                SELECT b.id,
                       b.title,
                       b.summary,
                       b.author,
                       b.genre,
                       b.isbn,
                       b.publication_year,
                       b.cover_image,
                       b.created_at,
                       b.updated_at,
                       AVG(r.rating) as rating,
                       COUNT(l1.id) as like_count,
                       CASE WHEN l2.id IS NOT NULL THEN true ELSE false END AS is_liked
                FROM books b
                         LEFT JOIN book_ratings r on b.id = r.book_id
                         LEFT JOIN book_likes l1 ON b.id = l1.book_id
                         LEFT JOIN book_likes l2 ON b.id = l2.book_id AND l2.user_id = :userId
                GROUP BY b.id
                ORDER BY b.rating DESC
                LIMIT :limit OFFSET :offset
                """;

        int offset = (page - 1) * size;

        Map<String, Object> params = Map.of(
                "userId", userId,
                "limit", size,
                "offset", offset
        );

        return namedParameterJdbcTemplate.query(sql, params, new BookResponseRowMapper());
    }

}

