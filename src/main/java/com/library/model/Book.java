package com.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Book extends AbstractEntity {

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String author;

    @Min(1900)
    @Column(nullable = false)
    private Integer publicationYear;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private Boolean available;

}

