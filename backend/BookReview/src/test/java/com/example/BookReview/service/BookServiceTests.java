package com.example.BookReview.service;

import com.example.BookReview.dto.request.BookRequest;
import com.example.BookReview.dto.response.BookResponse;
import com.example.BookReview.dto.response.PageableResponse;
import com.example.BookReview.exception.GlobalException;
import com.example.BookReview.models.Book;
import com.example.BookReview.repository.BookRepository;
import com.example.BookReview.util.Helper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    void testGetAllBooks_Success() throws GlobalException {
        // Arrange
        Book book = new Book();
        book.setId(1);
        book.setTitle("Book A");

        Page<Book> mockPage = new PageImpl<>(List.of(book));

        BookResponse bookResponse = new BookResponse();
        bookResponse.setId(1);
        bookResponse.setTitle("Book A");

        Mockito.when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockPage);
        Mockito.when(bookRepository.getBookRatingByBookId(1)).thenReturn(Optional.of(4.0));
        Mockito.when(bookRepository.getBookLikesCountByBookId(1)).thenReturn(5);

        // Mock static method for Helper
        try (MockedStatic<Helper> helperMock = Mockito.mockStatic(Helper.class)) {
            PageableResponse<BookResponse> pageableResponse = new PageableResponse<>();
            pageableResponse.setData(new ArrayList<>(List.of(bookResponse)));

            helperMock.when(() -> Helper.getPageableResponse(mockPage, BookResponse.class))
                    .thenReturn(pageableResponse);

            // Act
            PageableResponse<BookResponse> result = bookService.getAllBooks(0, 10, "title", "asc");

            // Assert
            assertEquals(1, result.getData().size());
            assertEquals(4.0, result.getData().get(0).getRating());
            assertEquals(5, result.getData().get(0).getLikeCount());
        }
    }

}
