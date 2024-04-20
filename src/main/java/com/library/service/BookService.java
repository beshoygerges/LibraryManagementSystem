package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Cacheable("books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(final Integer id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    @CacheEvict(value = "books", key = "#id")
    public void deleteBookById(Integer id) {
        if (bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }

    @CachePut(value = "books", key = "#result.id")
    public Book add(Book book) {
        if (Strings.isBlank(book.getAuthor())) {
            throw new RuntimeException("Author is required");
        }

        if (Strings.isBlank(book.getIsbn())) {
            throw new RuntimeException("ISBN is required");
        }

        if (Strings.isBlank(book.getTitle())) {
            throw new RuntimeException("Title is required");
        }

        if (Objects.isNull(book.getPublicationYear())) {
            throw new RuntimeException("Year is required");
        }

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new RuntimeException("ISBN is already exist");
        }

        book.setUuid(UUID.randomUUID());
        book.setAvailable(true);

        return bookRepository.save(book);
    }

    @CachePut(value = "books", key = "#result.id")
    public Book updateBook(final Integer id, final Book updatedBook) {
        if (Objects.isNull(id)) {
            throw new RuntimeException("Id is required");
        }

        if (Objects.isNull(updatedBook)) {
            throw new RuntimeException("Updated Book is required");
        }

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setIsbn(updatedBook.getIsbn());

        return bookRepository.save(existingBook);
    }

}
