
// BorrowerService.java
package com.aeon.library.service;

import com.aeon.library.dto.BorrowerDto;
import java.util.List;

public interface BorrowerService {
    BorrowerDto registerBorrower(BorrowerDto borrowerDTO);
    void borrowBook(Long borrowerId, Long bookId);
    void returnBook(Long borrowerId, Long bookId);
    List<BorrowerDto> getAllBorrowers();

    BorrowerDto getBorrowerById(Long borrowerId);
}