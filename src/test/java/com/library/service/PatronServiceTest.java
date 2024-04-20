package com.library.service;

import com.library.model.Patron;
import com.library.repository.PatronRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;


    @Test
    void testGetAllPatrons() {
        // Arrange
        when(patronRepository.findAll()).thenReturn(List.of(new Patron(), new Patron()));

        // Act
        List<Patron> patrons = patronService.getAllPatrons();

        // Assert
        assertNotNull(patrons);
        assertEquals(2, patrons.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById() {
        // Arrange
        Integer patronId = 1;
        Patron patron = new Patron();
        patron.setId(patronId);
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        // Act
        Patron foundPatron = patronService.getPatronById(patronId);

        // Assert
        assertNotNull(foundPatron);
        assertEquals(patronId, foundPatron.getId());
        verify(patronRepository, times(1)).findById(patronId);
    }

    @Test
    void testAddPatron() {
        // Arrange
        Patron patronToAdd = new Patron();
        patronToAdd.setEmail("test@example.com");
        patronToAdd.setMobile("1234567890");

        when(patronRepository.existsByEmailIgnoreCase(patronToAdd.getEmail())).thenReturn(false);
        when(patronRepository.existsByMobile(patronToAdd.getMobile())).thenReturn(false);
        when(patronRepository.save(patronToAdd)).thenReturn(patronToAdd);

        // Act
        Patron addedPatron = patronService.addPatron(patronToAdd);

        // Assert
        assertNotNull(addedPatron);
        assertNotNull(addedPatron.getUuid());
        verify(patronRepository, times(1)).save(patronToAdd);
    }

    @Test
    void testAddPatronShouldFailEmailExist() {
        // Arrange
        Patron patronToAdd = new Patron();
        patronToAdd.setEmail("test@example.com");
        patronToAdd.setMobile("1234567890");

        when(patronRepository.existsByEmailIgnoreCase(patronToAdd.getEmail())).thenReturn(true);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> patronService.addPatron(patronToAdd));
        verify(patronRepository, times(0)).save(patronToAdd);
    }

    @Test
    void testAddPatronShouldFailMobileExist() {
        // Arrange
        Patron patronToAdd = new Patron();
        patronToAdd.setEmail("test@example.com");
        patronToAdd.setMobile("1234567890");

        when(patronRepository.existsByEmailIgnoreCase("test@example.com")).thenReturn(false);
        when(patronRepository.existsByMobile(patronToAdd.getMobile())).thenReturn(true);

        // Act
        // Assert
        assertThrows(RuntimeException.class, () -> patronService.addPatron(patronToAdd));
        verify(patronRepository, times(0)).save(patronToAdd);
    }

    @Test
    void testUpdatePatron() {
        // Arrange
        Integer patronId = 1;
        Patron existingPatron = new Patron();
        existingPatron.setId(patronId);
        existingPatron.setName("Old Name");
        existingPatron.setEmail("old@example.com");
        existingPatron.setMobile("1234567890");

        Patron updatedPatronData = new Patron();
        updatedPatronData.setName("New Name");
        updatedPatronData.setEmail("new@example.com");
        updatedPatronData.setMobile("9876543210");

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(existingPatron)).thenReturn(existingPatron);

        // Act
        Patron updatedPatron = patronService.updatePatron(patronId, updatedPatronData);

        // Assert
        assertNotNull(updatedPatron);
        assertEquals(updatedPatronData.getName(), updatedPatron.getName());
        assertEquals(updatedPatronData.getEmail(), updatedPatron.getEmail());
        assertEquals(updatedPatronData.getMobile(), updatedPatron.getMobile());
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).save(existingPatron);
    }

    @Test
    void testDeletePatron() {
        // Arrange
        Integer patronId = 1;
        Patron existingPatron = new Patron();
        existingPatron.setId(patronId);
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));

        // Act
        patronService.deletePatron(patronId);

        // Assert
        verify(patronRepository, times(1)).delete(existingPatron);
    }

    @Test
    void testDeleteAllPatrons() {
        // Act
        patronService.deleteAllPatrons();

        // Assert
        verify(patronRepository, times(1)).deleteAll();
    }
}

