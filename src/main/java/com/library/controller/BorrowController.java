package com.library.controller;

import com.library.model.BorrowHistory;
import com.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/api/v1/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowHistory> borrowBook(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("patronId") Integer patronId) {
        BorrowHistory borrowHistory = borrowService.borrowBook(bookId, patronId);
        return new ResponseEntity<>(borrowHistory, HttpStatus.OK);
    }

    @PutMapping("/api/v1/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowHistory> returnBook(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("patronId") Integer patronId) {
        BorrowHistory borrowHistory = borrowService.returnBook(bookId, patronId);
        return new ResponseEntity<>(borrowHistory, HttpStatus.OK);
    }
}
