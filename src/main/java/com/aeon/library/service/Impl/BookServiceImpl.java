package com.aeon.library.service.Impl;

import com.aeon.library.dto.BookDto;
import com.aeon.library.entities.Book;
import com.aeon.library.entities.Borrower;
import com.aeon.library.exception.ArgumentNotValidException;
import com.aeon.library.exception.BookNotFoundException;
import com.aeon.library.exception.BorrowerNotFoundException;
import com.aeon.library.repository.BookRepository;
import com.aeon.library.repository.BorrowerRepository;
import com.aeon.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final BorrowerRepository borrowerRepository;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public BookDto registerBook(BookDto bookDto) {
        // Check if a book with the same ISBN already exists and validate
        checkForExistingBookWithSameIsbn(bookDto);

        // Map the DTO to the entity
        Book book = modelMapper.map(bookDto, Book.class);

        // Handle borrower association if provided
        book.setBorrower(getBorrowerIfExists(bookDto.getBorrowerId()));

        // Save the book and map the saved entity back to DTO
        Book savedBook = bookRepository.save(book);
        return modelMapper.map(savedBook, BookDto.class);
    }

    @Override
    public BookDto getBookById(Long bookId) {
        // Fetch the book by its ID and map it to DTO
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
        return modelMapper.map(book, BookDto.class);
    }
    @Override
    public List<BookDto> getAllBooks() {
        // Map all books to DTOs in a streamlined way
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }



    private void checkForExistingBookWithSameIsbn(BookDto bookDto) {
        // Fetch existing books with the same ISBN
        List<Book> existingBooks = bookRepository.findByIsbn(bookDto.getIsbn());

        // Validate that all books with the same ISBN have the same title and author
        existingBooks.stream()
                .filter(existingBook ->
                        !Objects.equals(existingBook.getTitle(), bookDto.getTitle()) ||
                                !Objects.equals(existingBook.getAuthor(), bookDto.getAuthor())
                )
                .findFirst()
                .ifPresent(book -> {
                    try {
                        throw new ArgumentNotValidException("Books with the same ISBN must have the same title and author");
                    } catch (ArgumentNotValidException e) {
                        throw new IllegalArgumentException(e);
                    }
                });
    }



    private Borrower getBorrowerIfExists(Long borrowerId) {
        // Only attempt to find the borrower if an ID is provided
        if (borrowerId != null && borrowerId > 0) {
            return borrowerRepository.findById(borrowerId)
                    .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with id: " + borrowerId));
        }
        return null;  // No borrower is associated
    }

}
