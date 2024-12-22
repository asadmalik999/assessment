package com.aeon.library.controller;

import com.aeon.library.dto.BorrowerDto;
import com.aeon.library.service.BorrowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private static final Logger logger = LoggerFactory.getLogger(BorrowerController.class);
    private final BorrowerService borrowerService;

    public BorrowerController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @PostMapping
    public ResponseEntity<BorrowerDto> registerBorrower(@RequestBody BorrowerDto borrowerDto) {
        logger.info("Creating new borrower: {}", borrowerDto.getName()); // Simplified log with meaningful information
        BorrowerDto savedBorrower = borrowerService.registerBorrower(borrowerDto);
        logger.info("Successfully created borrower with ID: {}", savedBorrower.getId());
        return new ResponseEntity<>(savedBorrower, HttpStatus.CREATED);
    }

    @PostMapping("/{borrowerId}/borrow/{bookId}")
    public ResponseEntity<String> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        logger.info("Borrow request - Borrower ID: {}, Book ID: {}", borrowerId, bookId);
        try {
            borrowerService.borrowBook(borrowerId, bookId);
            logger.info("Borrower ID: {} successfully borrowed Book ID: {}", borrowerId, bookId);
            return ResponseEntity.ok("Book borrowed successfully");
        } catch (Exception e) {
            logger.error("Error borrowing Book ID: {} for Borrower ID: {}", bookId, borrowerId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error borrowing book: " + e.getMessage());
        }
    }

    @PostMapping("/{borrowerId}/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
        logger.info("Return request - Borrower ID: {}, Book ID: {}", borrowerId, bookId);
        try {
            borrowerService.returnBook(borrowerId, bookId);
            logger.info("Borrower ID: {} successfully returned Book ID: {}", borrowerId, bookId);
            return ResponseEntity.ok("Book returned successfully");
        } catch (Exception e) {
            logger.error("Error returning Book ID: {} for Borrower ID: {}", bookId, borrowerId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error returning book: " + e.getMessage());
        }
    }

    @GetMapping("/{borrowerId}")
    public ResponseEntity<BorrowerDto> getBorrowerDetails(@PathVariable Long borrowerId) {
        logger.info("Fetching details for Borrower ID: {}", borrowerId);
        BorrowerDto borrowerDTO = borrowerService.getBorrowerById(borrowerId);
        if (borrowerDTO != null) {
            logger.info("Fetched details for Borrower ID: {}", borrowerId);
            return ResponseEntity.ok(borrowerDTO);
        } else {
            logger.warn("Borrower with ID: {} not found", borrowerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<BorrowerDto>> getAllBorrowers() {
        logger.info("Fetching all borrowers");
        List<BorrowerDto> borrowers = borrowerService.getAllBorrowers();
        logger.info("Fetched {} borrowers", borrowers.size());
        return ResponseEntity.ok(borrowers);
    }


}
