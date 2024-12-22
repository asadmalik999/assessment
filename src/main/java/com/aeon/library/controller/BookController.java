package com.aeon.library.controller;

import com.aeon.library.dto.BookDto;
import com.aeon.library.exception.ArgumentNotValidException;
import com.aeon.library.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> registerBook(@RequestBody BookDto bookDto) throws ArgumentNotValidException {
        logger.debug("Processing request to create book: {}", bookDto);

        // Validating the incoming request data before passing it to the service
        if (bookDto == null || bookDto.getTitle() == null || bookDto.getTitle().isEmpty()) {
            logger.warn("Invalid book data: Title is required.");
            throw new ArgumentNotValidException("Book title cannot be null or empty.");
        }

        try {
            BookDto savedBook = bookService.registerBook(bookDto);
            logger.info("Book created successfully with ID: {}", savedBook.getId());
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while registering the book: {}", bookDto, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        logger.debug("Retrieving all books from the database");
        List<BookDto> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            logger.info("No books found");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable Long bookId) {
        logger.debug("Fetching details for book with ID: {}", bookId);
        BookDto bookDto = bookService.getBookById(bookId);
        if (bookDto != null) {
            logger.info("Found book details for ID: {}", bookId);
            return ResponseEntity.ok(bookDto);
        } else {
            logger.warn("Book with ID {} not found", bookId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
