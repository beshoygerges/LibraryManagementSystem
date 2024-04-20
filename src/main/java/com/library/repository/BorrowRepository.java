package com.library.repository;

import com.library.model.Book;
import com.library.model.BorrowHistory;
import com.library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowHistory, Long> {
    Optional<BorrowHistory> findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
}