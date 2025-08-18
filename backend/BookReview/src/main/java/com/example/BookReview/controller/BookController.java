package com.example.BookReview.controller;

import com.example.BookReview.dto.request.BookCommentRequest;
import com.example.BookReview.dto.request.BookRatingRequest;
import com.example.BookReview.dto.request.BookRequest;
import com.example.BookReview.dto.response.ApiResponse;
import com.example.BookReview.dto.response.BookCommentResponse;
import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.BookService;
import com.example.BookReview.service.auth.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {


    @Autowired
    private BookService bookService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) throws GlobalException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookService.createBook(bookRequest)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) throws GlobalException {
        return ResponseEntity.ok(
                bookService.getBookById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(
            @RequestParam(name = "pageNum", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws GlobalException {
        long userId = userDetails.getId();

        return ResponseEntity.ok(
                bookService.getAllBooks(userId, pageNumber, pageSize, sortBy, sortDir)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @Valid @RequestBody BookRequest bookRequest, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable int id) throws GlobalException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("Book Deleted Successfully!")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<PageableResponse<BookResponse>> searchBook(
            @PathVariable String searchString,
            @RequestParam(name = "pageNum", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) throws GlobalException {
        pageNumber--;
        return ResponseEntity.ok(
                bookService.searchBook(searchString, pageNumber, pageSize, sortBy, sortDir)
        );
    }


    @PostMapping("/comments")
    public ResponseEntity<ApiResponse> createComment(@Valid @RequestBody BookCommentRequest bookCommentRequest, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {

        long userId = userDetails.getId();
        bookService.createComment(userId, bookCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .msg("Comment Is Created Successfully!")
                        .httpStatus(HttpStatus.CREATED)
                        .build()
        );
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable int commentId, @Valid @RequestBody BookCommentRequest bookCommentRequest, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {

        long userId = userDetails.getId();
        bookService.updateComment(userId, commentId, bookCommentRequest);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("Comment Is Updated Successfully!")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );
    }


    @GetMapping("/comments/{bookId}")
    public ResponseEntity<PageableResponse<BookCommentResponse>> getAllComments(
            @PathVariable int bookId,
            @RequestParam(name = "pageNum", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc", required = false) String sortDir
    ) throws GlobalException {
        pageNumber--;
        return ResponseEntity.ok(
                bookService.getAllComments(bookId, pageNumber, pageSize, sortBy, sortDir)
        );
    }

    @PostMapping("/like/{bookId}")
    public ResponseEntity<ApiResponse> likeTheBook(@PathVariable int bookId, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {

        long userId = userDetails.getId();

        bookService.likeTheBook(userId, bookId);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("OK")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );

    }


    @PostMapping("/rating/{bookId}")
    public ResponseEntity<ApiResponse> addRatingForTheBook(@PathVariable int bookId, @RequestBody BookRatingRequest bookRatingRequest, @AuthenticationPrincipal CustomUserDetails userDetails) throws GlobalException {

        long userId = userDetails.getId();

        bookService.addRatingForTheBook(userId, bookId, bookRatingRequest.getRating());

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .msg("OK")
                        .httpStatus(HttpStatus.OK)
                        .build()
        );

    }


}
