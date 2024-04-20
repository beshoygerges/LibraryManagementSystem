package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @DisplayName("JUnit test for get all books method")
    @Test
    void testGetAllBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookService.getAllBooks();

        // Assert
        assertEquals(expectedBooks.size(), actualBooks.size());
    }

    @DisplayName("JUnit test for get book by id method")
    @Test
    void testGetBookById() {
        // Arrange
        Integer id = 1;
        Book expectedBook = new Book();
        expectedBook.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(expectedBook));

        // Act
        Book actualBook = bookService.getBookById(id);

        // Assert
        assertNotNull(actualBook);
        assertEquals(id, actualBook.getId());
    }

    @DisplayName("JUnit test for get book by id method should throw")
    @Test
    void testGetBookByIdShouldThrow() {
        // Arrange
        Integer id = 1;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(RuntimeException.class, () -> bookService.getBookById(id));

    }

    @Test
    void testDeleteBookByIdShouldThrow() {
        // Arrange
        Integer id = 1;
        when(bookRepository.existsById(id)).thenReturn(true);

        // Act
        assertThrows(RuntimeException.class, () -> bookService.deleteBookById(id));

        // Assert
        verify(bookRepository, never()).deleteById(id);
    }

    @Test
    void testDeleteBookById() {
        // Arrange
        Integer id = 1;
        when(bookRepository.existsById(id)).thenReturn(false);

        // Act
        bookService.deleteBookById(id);

        // Assert
        verify(bookRepository, atMostOnce()).deleteById(id);
    }

    @Test
    void testAddBook() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("Title");
        bookToAdd.setAuthor("Author");
        bookToAdd.setIsbn("ISBN");
        bookToAdd.setPublicationYear(2022);

        when(bookRepository.existsByIsbn("ISBN")).thenReturn(false);
        when(bookRepository.save(bookToAdd)).thenReturn(bookToAdd);

        // Act
        Book addedBook = bookService.add(bookToAdd);

        // Assert
        assertNotNull(addedBook);
        assertNotNull(addedBook.getUuid());
        assertTrue(addedBook.getAvailable());
        verify(bookRepository, times(1)).save(bookToAdd);
    }

    @Test
    void testAddBookShouldThrow() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("Title");
        bookToAdd.setAuthor("Author");
        bookToAdd.setIsbn("ISBN");
        bookToAdd.setPublicationYear(2022);

        when(bookRepository.existsByIsbn("ISBN")).thenReturn(true);


        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> bookService.add(bookToAdd));
        verify(bookRepository, times(0)).save(bookToAdd);
    }

    @Test
    void testAddBookShouldThrowEmptyTitle() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("");
        bookToAdd.setAuthor("Author");
        bookToAdd.setIsbn("ISBN");
        bookToAdd.setPublicationYear(2022);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> bookService.add(bookToAdd));
        verify(bookRepository, times(0)).save(bookToAdd);
    }

    @Test
    void testAddBookShouldThrowEmptyAuthor() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("title");
        bookToAdd.setAuthor("");
        bookToAdd.setIsbn("ISBN");
        bookToAdd.setPublicationYear(2022);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> bookService.add(bookToAdd));
        verify(bookRepository, times(0)).save(bookToAdd);
    }

    @Test
    void testAddBookShouldThrowEmptyISBN() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("title");
        bookToAdd.setAuthor("author");
        bookToAdd.setIsbn("");
        bookToAdd.setPublicationYear(2022);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> bookService.add(bookToAdd));
        verify(bookRepository, times(0)).save(bookToAdd);
    }

    @Test
    void testAddBookShouldThrowEmptyYear() {
        // Arrange
        Book bookToAdd = new Book();
        bookToAdd.setTitle("title");
        bookToAdd.setAuthor("author");
        bookToAdd.setIsbn("ISBN");
        bookToAdd.setPublicationYear(null);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> bookService.add(bookToAdd));
        verify(bookRepository, times(0)).save(bookToAdd);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Integer bookId = 1;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setIsbn("Old ISBN");
        existingBook.setPublicationYear(2020);

        Book updatedBookData = new Book();
        updatedBookData.setTitle("New Title");
        updatedBookData.setAuthor("New Author");
        updatedBookData.setIsbn("New ISBN");
        updatedBookData.setPublicationYear(2021);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Book updatedBook = bookService.updateBook(bookId, updatedBookData);

        // Assert
        assertNotNull(updatedBook);
        assertEquals(updatedBookData.getTitle(), updatedBook.getTitle());
        assertEquals(updatedBookData.getAuthor(), updatedBook.getAuthor());
        assertEquals(updatedBookData.getIsbn(), updatedBook.getIsbn());
        assertEquals(updatedBookData.getPublicationYear(), updatedBook.getPublicationYear());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBookShouldFailBookNotFound() {
        // Arrange
        Integer bookId = 1;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setIsbn("Old ISBN");
        existingBook.setPublicationYear(2020);

        Book updatedBookData = new Book();
        updatedBookData.setTitle("New Title");
        updatedBookData.setAuthor("New Author");
        updatedBookData.setIsbn("New ISBN");
        updatedBookData.setPublicationYear(2021);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());


        // Act
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookId, updatedBookData));

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(0)).save(existingBook);
    }

    @Test
    void testUpdateBookShouldFailNullId() {
        // Arrange
        Integer bookId = 1;

        Book updatedBookData = new Book();
        updatedBookData.setTitle("New Title");
        updatedBookData.setAuthor("New Author");
        updatedBookData.setIsbn("New ISBN");
        updatedBookData.setPublicationYear(2021);


        // Act
        assertThrows(RuntimeException.class, () -> bookService.updateBook(null, updatedBookData));

        // Assert
        verify(bookRepository, times(0)).findById(bookId);
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void testUpdateBookShouldFailNullUpdatedBook() {
        // Arrange
        Integer bookId = 1;

        // Act
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookId, null));

        // Assert
        verify(bookRepository, times(0)).findById(bookId);
        verify(bookRepository, times(0)).save(any(Book.class));
    }
}

