package com.library.service;

import com.library.model.Book;
import com.library.model.BorrowHistory;
import com.library.model.Patron;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;


    @Transactional
    public BorrowHistory borrowBook(Integer bookId, Integer patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("book not found " + bookId));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("patron not found " + bookId));

        if (Boolean.FALSE.equals(book.getAvailable())) {
            throw new RuntimeException("Book isn't available right now");
        }

        // Update book availability status
        book.setAvailable(false);
        bookRepository.save(book);

        // Create borrowing record
        BorrowHistory borrowingRecord = new BorrowHistory();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setUuid(UUID.randomUUID());
        return borrowRepository.save(borrowingRecord);
    }


    @Transactional
    public BorrowHistory returnBook(Integer bookId, Integer patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("book not found " + bookId));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("patron not found " + bookId));

        if (Boolean.TRUE.equals(book.getAvailable())) {
            throw new RuntimeException("book already available");
        }
        // Update book availability status
        book.setAvailable(true);

        // Find the borrowing record for the book and patron
        BorrowHistory borrowingRecord = borrowRepository.findByBookAndPatronAndReturnDateIsNull(book, patron).orElseThrow(() -> new RuntimeException("No borrow history found"));

        borrowingRecord.setReturnDate(LocalDate.now());
        bookRepository.save(book);
        return borrowRepository.save(borrowingRecord);
    }
}

