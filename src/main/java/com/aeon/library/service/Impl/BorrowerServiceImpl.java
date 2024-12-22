package com.aeon.library.service.Impl;

import com.aeon.library.dto.BorrowerDto;
import com.aeon.library.entities.Book;
import com.aeon.library.entities.Borrower;
import com.aeon.library.exception.*;
import com.aeon.library.repository.BookRepository;
import com.aeon.library.repository.BorrowerRepository;
import com.aeon.library.service.BorrowerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BookRepository bookRepository, ModelMapper modelMapper) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BorrowerDto registerBorrower(BorrowerDto borrowerDto) {
        if (borrowerRepository.existsByEmail(borrowerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Borrower with this email already exists.");
        }
        Borrower savedBorrower = borrowerRepository.save(modelMapper.map(borrowerDto, Borrower.class));
        return modelMapper.map(savedBorrower, BorrowerDto.class);
    }

    @Override
    public void borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = findBorrower(borrowerId);
        Book book = findBook(bookId);

        if (book.getBorrower() != null) {
            throw new BookAlreadyIssuedException("Book is already borrowed.");
        }

        book.setBorrower(borrower);
        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long borrowerId, Long bookId) {
        Borrower borrower = findBorrower(borrowerId);
        Book book = findBook(bookId);

        if (book.getBorrower() == null || !book.getBorrower().equals(borrower)) {
            throw new BookNotBorrowedException("This book was not borrowed by this borrower.");
        }

        book.setBorrower(null);
        bookRepository.save(book);
    }

    @Override
    public List<BorrowerDto> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(borrower -> modelMapper.map(borrower, BorrowerDto.class))
                .toList();
    }

    @Override
    public BorrowerDto getBorrowerById(Long borrowerId) {
        Borrower borrower = findBorrower(borrowerId);
        return modelMapper.map(borrower, BorrowerDto.class);
    }

    private Borrower findBorrower(Long borrowerId) {
        return borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with ID: " + borrowerId));
    }

    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
    }
}
