package com.example.BookReview.service;

import com.example.BookReview.dto.request.BookRequest;
import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.Book;
import com.example.BookReview.models.BookComment;
import com.example.BookReview.models.User;
import com.example.BookReview.repository.BookCommentRepository;
import com.example.BookReview.repository.BookLikeRepository;
import com.example.BookReview.repository.BookRatingRepository;
import com.example.BookReview.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookCommentRepository bookCommentRepository;

    @Mock
    private BookLikeRepository bookLikeRepository;

    @Mock
    private BookRatingRepository bookRatingRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteBook_Success() throws GlobalException {
        int bookId = 1;

        // Mock: book exists
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        // Act
        bookService.deleteBook(bookId);

        // Assert: Verify correct repository calls
        verify(bookCommentRepository, times(1)).deleteAllByBook_Id(bookId);
        verify(bookLikeRepository, times(1)).deleteAllByBookId(bookId);
        verify(bookRatingRepository, times(1)).deleteAllByBookId(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_NotFound() {
        int bookId = 99;

        // Mock: book not found
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        GlobalException ex = assertThrows(GlobalException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertTrue(ex.getMessage().contains("Book Delete Failed - Book Not Found Exception"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());

        // Ensure nothing else was called
        verify(bookCommentRepository, never()).deleteAllByBook_Id(anyInt());
        verify(bookLikeRepository, never()).deleteAllByBookId(anyInt());
        verify(bookRatingRepository, never()).deleteAllByBookId(anyInt());
        verify(bookRepository, never()).deleteById(anyInt());
    }

    @Test
    void testDeleteBook_RepositoryThrowsError() {
        int bookId = 2;

        // Mock: book exists
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        // Mock: failure when deleting comments
        doThrow(new RuntimeException("DB error")).when(bookCommentRepository).deleteAllByBook_Id(bookId);

        // Act & Assert
        GlobalException ex = assertThrows(GlobalException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertTrue(ex.getMessage().contains("Book Delete Failed - DB error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());
    }

    @Test
    void createBook_ShouldReturnBookResponse_WhenSuccess() throws GlobalException {
        // Arrange
        BookRequest request = new BookRequest();
        request.setTitle("Test Book");

        Book book = new Book();
        book.setTitle("Test Book");

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setTitle("Test Book");

        BookResponse expectedResponse = new BookResponse();
        expectedResponse.setId(1);
        expectedResponse.setTitle("Test Book");

        // Mock mapping and repository
        when(modelMapper.map(request, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(modelMapper.map(savedBook, BookResponse.class)).thenReturn(expectedResponse);

        // Act
        BookResponse actualResponse = bookService.createBook(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(3, actualResponse.getRating());
        assertEquals(0, actualResponse.getLikeCount());
        assertEquals("Test Book", actualResponse.getTitle());

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_ShouldThrowGlobalException_WhenSaveFails() {
        // Arrange
        BookRequest request = new BookRequest();
        Book book = new Book();

        when(modelMapper.map(request, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        GlobalException ex = assertThrows(GlobalException.class, () -> bookService.createBook(request));
        assertTrue(ex.getMessage().contains("Book Creation Failed"));
    }

    @Test
    void testGetBookById_Success() throws GlobalException {
        // Arrange
        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");

        BookResponse response = new BookResponse();
        response.setId(1);
        response.setTitle("Test Book");

        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        Mockito.when(modelMapper.map(book, BookResponse.class)).thenReturn(response);
        Mockito.when(bookRepository.getBookRatingByBookId(1)).thenReturn(Optional.of(4.5));
        Mockito.when(bookRepository.getBookLikesCountByBookId(1)).thenReturn(10);

        // Act
        BookResponse result = bookService.getBookById(1);

        // Assert
        assertEquals(1, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals(4.5, result.getRating());
        assertEquals(10, result.getLikeCount());
    }

    @Test
    void testGetBookById_NotFound() {
        Mockito.when(bookRepository.findById(99)).thenReturn(Optional.empty());

        GlobalException ex = assertThrows(GlobalException.class, () -> bookService.getBookById(99));
        assertTrue(ex.getMessage().contains("Book Not Found"));
    }

//    @Test
//    void testGetAllBooks_Success() throws GlobalException {
//        // Arrange
//        Book book = new Book();
//        book.setId(1);
//        book.setTitle("Book A");
//
//        Page<Book> mockPage = new PageImpl<>(List.of(book));
//
//        BookResponse bookResponse = new BookResponse();
//        bookResponse.setId(1);
//        bookResponse.setTitle("Book A");
//
//        Mockito.when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockPage);
//        Mockito.when(bookRepository.getBookRatingByBookId(1)).thenReturn(Optional.of(4.0));
//        Mockito.when(bookRepository.getBookLikesCountByBookId(1)).thenReturn(5);
//
//        // Mock static method for Helper
//        try (MockedStatic<Helper> helperMock = Mockito.mockStatic(Helper.class)) {
//            PageableResponse<BookResponse> pageableResponse = new PageableResponse<>();
//            pageableResponse.setData(new ArrayList<>(List.of(bookResponse)));
//
//            helperMock.when(() -> Helper.getPageableResponse(mockPage, BookResponse.class))
//                    .thenReturn(pageableResponse);
//
//            // Act
//            PageableResponse<BookResponse> result = bookService.getAllBooks(0, 10, "title", "asc");
//
//            // Assert
//            assertEquals(1, result.getData().size());
//            assertEquals(4.0, result.getData().get(0).getRating());
//            assertEquals(5, result.getData().get(0).getLikeCount());
//        }
//    }


    @Test
    void deleteComment_Success() throws GlobalException {
        // Arrange
        int userId = 1;
        int commentId = 101;

        BookComment mockComment = new BookComment();
        User mockUser = new User();
        mockUser.setId((long) userId);
        mockComment.setUser(mockUser);

        when(bookCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // Act
        bookService.deleteComment(userId, commentId);

        // Assert
        verify(bookCommentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void deleteComment_CommentNotFound() {
        // Arrange
        int userId = 1;
        int commentId = 101;

        when(bookCommentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        GlobalException exception = assertThrows(GlobalException.class,
                () -> bookService.deleteComment(userId, commentId));

        assertEquals("Comment Not Present!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(bookCommentRepository, never()).deleteById(anyInt());
    }

    @Test
    void deleteComment_UserIdMismatch() {
        // Arrange
        int userId = 1;
        int commentId = 101;

        BookComment mockComment = new BookComment();
        User mockUser = new User();
        mockUser.setId((long) 999); // Different user
        mockComment.setUser(mockUser);

        when(bookCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // Act & Assert
        GlobalException exception = assertThrows(GlobalException.class,
                () -> bookService.deleteComment(userId, commentId));

        assertTrue(exception.getMessage().contains("Comment Delete Exception"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        verify(bookCommentRepository, never()).deleteById(anyInt());
    }
}
