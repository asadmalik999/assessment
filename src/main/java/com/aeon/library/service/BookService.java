package com.aeon.library.service;
import com.aeon.library.dto.BookDto;
import com.aeon.library.exception.ArgumentNotValidException;

import java.util.List;

public interface BookService {
    BookDto registerBook(BookDto bookDto) throws ArgumentNotValidException;
    List<BookDto> getAllBooks();

    BookDto getBookById(Long bookId);
}
