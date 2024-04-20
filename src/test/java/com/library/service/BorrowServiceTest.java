package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowHistory;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowService borrowService;


    @Test
    void testBorrowBook() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        Patron patron = new Patron();
        patron.setId(patronId);

        BorrowHistory borrowingRecord = new BorrowHistory();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setUuid(UUID.randomUUID());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowRepository.save(any(BorrowHistory.class))).thenReturn(borrowingRecord);

        // Act
        BorrowHistory borrowedBook = borrowService.borrowBook(bookId, patronId);

        // Assert
        assertNotNull(borrowedBook);
        assertFalse(book.getAvailable());
        assertEquals(book, borrowedBook.getBook());
        assertEquals(patron, borrowedBook.getPatron());
        assertNotNull(borrowedBook.getBorrowDate());
        assertNotNull(borrowedBook.getUuid());
        verify(bookRepository, times(1)).save(book);
        verify(borrowRepository, times(1)).save(any(BorrowHistory.class));
    }

    @Test
    void testBorrowBookShouldFailBookNotAvailable() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);

        Patron patron = new Patron();
        patron.setId(patronId);


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> borrowService.borrowBook(bookId, patronId));

        verify(bookRepository, times(0)).save(book);
        verify(borrowRepository, times(0)).save(any(BorrowHistory.class));
    }

    @Test
    void testReturnBook() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);

        Patron patron = new Patron();
        patron.setId(patronId);

        BorrowHistory borrowingRecord = new BorrowHistory();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)).thenReturn(Optional.of(borrowingRecord));
        when(borrowRepository.save(any(BorrowHistory.class))).thenReturn(borrowingRecord);

        // Act
        BorrowHistory returnedBook = borrowService.returnBook(bookId, patronId);

        // Assert
        assertNotNull(returnedBook);
        assertTrue(book.getAvailable());
        assertEquals(book, returnedBook.getBook());
        assertEquals(patron, returnedBook.getPatron());
        assertNotNull(returnedBook.getReturnDate());
        verify(bookRepository, times(1)).save(book);
        verify(borrowRepository, times(1)).save(borrowingRecord);
    }

    @Test
    void testReturnBookShouldFailBookIsAvailable() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        Patron patron = new Patron();
        patron.setId(patronId);


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));


        // Act
        assertThrows(RuntimeException.class, () -> borrowService.returnBook(bookId, patronId));

        // Assert
        verify(bookRepository, times(0)).save(book);
        verify(borrowRepository, times(0)).save(any(BorrowHistory.class));
    }

    @Test
    void testReturnBookShouldFailBorrowNotFound() {
        // Arrange
        Integer bookId = 1;
        Integer patronId = 1;

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(false);

        Patron patron = new Patron();
        patron.setId(patronId);


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowRepository.findByBookAndPatronAndReturnDateIsNull(book, patron)).thenReturn(Optional.empty());


        // Act
        assertThrows(RuntimeException.class, () -> borrowService.returnBook(bookId, patronId));

        // Assert
        verify(bookRepository, times(0)).save(book);
        verify(borrowRepository, times(0)).save(any(BorrowHistory.class));
    }
}

