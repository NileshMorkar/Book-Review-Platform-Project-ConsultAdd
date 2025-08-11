package com.example.BookReview.controller;

import com.example.BookReview.dto.request.BookRequest;
import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {


    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) throws GlobalException {
        return ResponseEntity.ok(bookService.createBook(bookRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) throws GlobalException {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<PageableResponse<BookResponse>> getAllBooks(
            @RequestParam(name = "pageNum", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) throws GlobalException {
        pageNumber--;
        return ResponseEntity.ok(bookService.getAllBooks(pageNumber, pageSize, sortBy, sortDir));
    }


    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @Valid @RequestBody BookRequest bookRequest) throws GlobalException {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
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
        return ResponseEntity.ok(bookService.searchBook(searchString, pageNumber, pageSize, sortBy, sortDir));
    }


//    @PostMapping("/comments")
//    public ResponseEntity<ApiResponse> createCommentOnBook(@Valid @RequestBody BookCommentRequest bookCommentRequest){
//
////        ResponseEntity.Ok
//
//    }

}
