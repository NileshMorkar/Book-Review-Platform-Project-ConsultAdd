package com.example.BookReview.service;

import com.example.BookReview.dto.request.BookCommentRequest;
import com.example.BookReview.dto.request.BookRequest;
import com.example.BookReview.dto.response.BookCommentResponse;
import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.Book;
import com.example.BookReview.models.BookComment;
import com.example.BookReview.models.User;
import com.example.BookReview.repository.*;
import com.example.BookReview.util.Helper;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    @Autowired
    private BookLikeRepository bookLikeRepository;

    @Autowired
    private BookRatingRepository bookRatingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


    public BookResponse createBook(BookRequest bookRequest) throws GlobalException {

        try {

            Book book = modelMapper.map(bookRequest, Book.class);

            log.info("Book Object => {}", book);

            book = bookRepository.save(book);

            log.info("Book Object => {}", book);

            BookResponse bookResponse = modelMapper.map(book, BookResponse.class);

            bookResponse.setRating(3);
            bookResponse.setLikeCount(0);

            return bookResponse;

        } catch (Exception e) {
            log.error("Book Creation Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Book Creation Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public BookResponse getBookById(int id) throws GlobalException {

        try {
            Optional<Book> bookOptional = bookRepository.findById(id);

            if (bookOptional.isEmpty())
                throw new Exception("Book Not Found Exception");

            Book book = bookOptional.get();

            BookResponse bookResponse = modelMapper.map(book, BookResponse.class);

            bookResponse.setRating(bookRepository.getBookRatingByBookId(book.getId()).orElse(0.0));
            bookResponse.setLikeCount(bookRepository.getBookLikesCountByBookId(book.getId()));

            return bookResponse;
        } catch (Exception e) {
            log.error("Get Book By Id Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Get Book By Id Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public PageableResponse<BookResponse> getAllBooks(int pageNumber, int pageSize, String sortBy, String sortDir) throws GlobalException {

        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
            Page<Book> page = bookRepository.findAll(pageRequest);

            log.info(page);

            PageableResponse<BookResponse> pageableResponse = Helper.getPageableResponse(page, BookResponse.class);

            pageableResponse.getData().forEach((bookResponse) -> {
                bookResponse.setRating(bookRepository.getBookRatingByBookId(bookResponse.getId()).orElse(3.0));
                bookResponse.setLikeCount(bookRepository.getBookLikesCountByBookId(bookResponse.getId()));
            });

            return pageableResponse;
        } catch (Exception e) {
            log.error("Get All Books Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Get All Books Failed - %s", e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    public BookResponse updateBook(int id, BookRequest bookRequest) throws GlobalException {
        try {

            Optional<Book> bookOptional = bookRepository.findById(id);

            if (bookOptional.isEmpty()) {
                throw new Exception("Book Not Found Exception");
            }

            log.info("Old Book Object {}", bookOptional.get());


            Book updatedBook = bookOptional.get();

            updatedBook.setTitle(bookRequest.getTitle());
            updatedBook.setAuthor(bookRequest.getAuthor());
            updatedBook.setCoverImage(bookRequest.getCoverImage());
            updatedBook.setSummary(bookRequest.getSummary());
            updatedBook.setPublicationYear(updatedBook.getPublicationYear());
            updatedBook.setCoverImage(updatedBook.getCoverImage());
            updatedBook.setGenre(updatedBook.getGenre());

            log.info("Updated Book Object => {}", updatedBook);
            updatedBook = bookRepository.save(updatedBook);

            BookResponse bookResponse = modelMapper.map(updatedBook, BookResponse.class);

            bookResponse.setRating(bookRepository.getBookRatingByBookId(updatedBook.getId()).orElse(0.0));
            bookResponse.setLikeCount(bookRepository.getBookLikesCountByBookId(updatedBook.getId()));

            return bookResponse;

        } catch (Exception e) {
            log.error("Book Update Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Book Update Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public PageableResponse<BookResponse> searchBook(String searchString, int pageNumber, int pageSize, String sortBy, String sortDir) throws GlobalException {
        try {
            if (StringUtils.isBlank(searchString)) {
                log.error("Search String Is Empty");
                throw new Exception("Search String Is Empty.");
            }

            searchString = searchString.trim();

            Sort sort = sortDir.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
            Page<Book> page = bookRepository.searchBook(searchString, pageRequest);

            log.info(page);

            PageableResponse<BookResponse> pageableResponse = Helper.getPageableResponse(page, BookResponse.class);

            pageableResponse.getData().forEach((bookResponse) -> {
                bookResponse.setRating(bookRepository.getBookRatingByBookId(bookResponse.getId()).orElse(3.0));
                bookResponse.setLikeCount(bookRepository.getBookLikesCountByBookId(bookResponse.getId()));
            });

            return pageableResponse;
        } catch (Exception e) {
            log.error("Search Books Failed for searchString - [{}], Exception - {}", searchString, e.getMessage());
            throw new GlobalException(String.format("Search Books Failed for searchString - %s, Exception  - %s", searchString, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void createComment(long userId, @Valid BookCommentRequest bookCommentRequest) throws GlobalException {

        Book book = bookRepository.findById(bookCommentRequest.getBookId()).orElseThrow(() -> new GlobalException("Book Id Is Not Present.", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException("User Id Is Not Present.", HttpStatus.NOT_FOUND));

        Optional<BookComment> bookCommentOptional = bookCommentRepository.findByUserIdAndBookId(userId, bookCommentRequest.getBookId());

        if (bookCommentOptional.isPresent())
            throw new GlobalException("Comment Is Already Present Given Book", HttpStatus.BAD_REQUEST);

        try {
            BookComment bookComment = BookComment.builder()
                    .book(book)
                    .user(user)
                    .commentMsg(bookCommentRequest.getCommentMsg())
                    .build();

            log.info("Comment Added For Book Id {}", bookCommentRequest.getBookId());
            bookCommentRepository.save(bookComment);
        } catch (Exception e) {
            log.error("Comment Add Exception - {}", e.getMessage());
            throw new GlobalException(String.format("Comment Add Exception - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteComment(int userId, int commentId) throws GlobalException {
        try {
            BookComment bookComment = bookCommentRepository.findById(commentId).orElseThrow(() -> new GlobalException("Comment Not Present!", HttpStatus.NOT_FOUND));

            if (bookComment.getUser().getId() != userId)
                throw new Exception("commentId Is Not valid");

            bookCommentRepository.deleteById(commentId);
        } catch (Exception e) {
            log.error("Comment Delete Exception - {}", e.getMessage());
            throw new GlobalException(String.format("Comment Delete Exception - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateComment(long userId, int commentId, BookCommentRequest bookCommentRequest) throws GlobalException {
        BookComment bookComment = bookCommentRepository.findById(commentId).orElseThrow(() -> new GlobalException("Comment Not Present!", HttpStatus.NOT_FOUND));

        try {
            if (bookComment.getUser().getId() != userId)
                throw new Exception("commentId Is Not valid!");

            bookComment.setCommentMsg(bookCommentRequest.getCommentMsg());

            bookCommentRepository.save(bookComment);
        } catch (Exception e) {
            log.error("Comment Update Exception - {}", e.getMessage());
            throw new GlobalException(String.format("Comment Update Exception - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public PageableResponse<BookCommentResponse> getAllComments(int bookId, int pageNumber, int pageSize, String sortBy, String sortDir) throws GlobalException {

        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

            Page<BookComment> page = bookCommentRepository.findAllByBook_Id(bookId, pageRequest);

            log.info(page);

            return Helper.getPageableResponse(page, BookCommentResponse.class);
        } catch (Exception e) {
            log.error("Get All Books Comments Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Get All Books Comment Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional
    public void deleteBook(int bookId) throws GlobalException {
        try {
            bookRepository.findById(bookId).orElseThrow(() -> new Exception("Book Not Found Exception"));

            bookCommentRepository.deleteAllByBookId(bookId);
            bookLikeRepository.deleteAllByBookId(bookId);
            bookRatingRepository.deleteAllByBookId(bookId);

            bookRepository.deleteById(bookId);

        } catch (Exception e) {
            log.error("Book Delete Failed - {}", e.getMessage());
            throw new GlobalException(String.format("Book Delete Failed - %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
