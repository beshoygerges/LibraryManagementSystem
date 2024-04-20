package com.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class BorrowHistory extends AbstractEntity {

    @ManyToOne
    private Book book;

    @ManyToOne
    private Patron patron;

    private LocalDate borrowDate;

    private LocalDate returnDate;
}

