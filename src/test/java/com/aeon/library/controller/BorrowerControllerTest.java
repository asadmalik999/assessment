package com.aeon.library.controller;

import com.aeon.library.dto.BorrowerDto;
import com.aeon.library.service.BorrowerService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowerControllerTest {

    @Mock
    private BorrowerService borrowerService;

    @InjectMocks
    private BorrowerController borrowerController;

    private BorrowerDto mockBorrowerDto;

    @BeforeEach
    void setUp() {
        // Using more realistic values for mock borrower data
        mockBorrowerDto = new BorrowerDto(1001L, "jack@gmail.com", "Jack");
    }

    @Test
    void shouldRegisterBorrowerAndReturnCreatedStatus() {
        // Mocking the borrower registration behavior
        when(borrowerService.registerBorrower(any(BorrowerDto.class))).thenReturn(mockBorrowerDto);

        // Calling the controller method to register a borrower
        ResponseEntity<BorrowerDto> responseEntity = borrowerController.registerBorrower(mockBorrowerDto);

        // Assertions with descriptive messages
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(), "Expected status to be CREATED when borrower is registered.");
        assertEquals(mockBorrowerDto, responseEntity.getBody(), "Expected response body to contain the registered borrower.");
    }

    @Test
    void shouldBorrowBookAndReturnSuccessMessage() {
        Long borrowerId = 1001L;
        Long bookId = 2001L;

        // Mocking the borrow book behavior
        doNothing().when(borrowerService).borrowBook(borrowerId, bookId);

        // Calling the controller method to borrow a book
        ResponseEntity<String> responseEntity = borrowerController.borrowBook(borrowerId, bookId);

        // Assertions with descriptive messages
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Expected status to be OK when borrowing a book.");
        assertEquals("Book borrowed successfully", responseEntity.getBody(), "Expected success message for borrowing the book.");
    }

    @Test
    void shouldReturnBookAndReturnSuccessMessage() {
        Long borrowerId = 1001L;
        Long bookId = 2001L;

        // Mocking the return book behavior
        doNothing().when(borrowerService).returnBook(borrowerId, bookId);

        // Calling the controller method to return a book
        ResponseEntity<String> responseEntity = borrowerController.returnBook(borrowerId, bookId);

        // Assertions with descriptive messages
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Expected status to be OK when returning a book.");
        assertEquals("Book returned successfully", responseEntity.getBody(), "Expected success message for returning the book.");
    }

    @Test
    void shouldReturnAllBorrowers() {
        // Mocking the behavior of the BorrowerService to return a list of borrowers
        List<BorrowerDto> mockBorrowers = List.of(mockBorrowerDto);
        when(borrowerService.getAllBorrowers()).thenReturn(mockBorrowers);

        // Calling the controller method to get all borrowers
        ResponseEntity<List<BorrowerDto>> responseEntity = borrowerController.getAllBorrowers();

        // Assertions with descriptive messages
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Expected status to be OK when retrieving all borrowers.");
        assertEquals(mockBorrowers, responseEntity.getBody(), "Expected response body to match the list of borrowers.");
    }
}
