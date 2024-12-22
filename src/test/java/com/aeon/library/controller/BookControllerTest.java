package com.aeon.library.controller;

import com.aeon.library.dto.BookDto;
import com.aeon.library.exception.ArgumentNotValidException;
import com.aeon.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto mockBookDto;

    @BeforeEach
    void setUp() {
        // Using more meaningful values for mock data
        mockBookDto = new BookDto(101L, "978-3-16-148410-0", "Spring Framework Essentials", "Jack", 2022L);
    }

    @Test
    void shouldRegisterBookAndReturnCreatedStatus() throws ArgumentNotValidException {
        // Mocking the book registration behavior of BookService
        when(bookService.registerBook(any(BookDto.class))).thenReturn(mockBookDto);

        // Call the controller method for book registration
        ResponseEntity<BookDto> responseEntity = bookController.registerBook(mockBookDto);

        // Verifying the status code and response body
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(), "Expected status to be CREATED.");
        assertEquals(mockBookDto, responseEntity.getBody(), "Expected response body to match the mocked BookDto.");
    }

    @Test
    void shouldReturnListOfBooks() {
        // Mocking the behavior of BookService to return a list of books
        List<BookDto> mockBooks = List.of(mockBookDto); // List containing our mockBookDto
        when(bookService.getAllBooks()).thenReturn(mockBooks);

        // Call the controller method to get all books
        ResponseEntity<List<BookDto>> responseEntity = bookController.getAllBooks();

        // Verifying the status code and response body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Expected status to be OK.");
        assertEquals(mockBooks, responseEntity.getBody(), "Expected the list of books to match the mocked list.");
    }
}
