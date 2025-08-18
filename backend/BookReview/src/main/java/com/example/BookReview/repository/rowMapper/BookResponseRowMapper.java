package com.example.BookReview.repository.rowMapper;

import com.example.BookReview.dto.response.BookResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookResponseRowMapper implements RowMapper<BookResponse> {

    @Override
    public BookResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BookResponse(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("summary"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getString("isbn"),
                rs.getInt("publication_year"),
                rs.getString("cover_image"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("like_count"),
                rs.getBoolean("is_liked"),
                rs.getDouble("rating")

        );
    }
}
