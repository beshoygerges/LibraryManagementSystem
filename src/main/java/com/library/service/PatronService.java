package com.library.service;

import com.library.model.Patron;
import com.library.repository.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PatronService {
    private final PatronRepository patronRepository;


    @Cacheable("patrons")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }


    public Patron getPatronById(Integer id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patron not found with id: " + id));
    }

    @CachePut(value = "patrons", key = "#patron.id")
    public Patron addPatron(Patron patron) {
        if (patronRepository.existsByEmailIgnoreCase(patron.getEmail())) {
            throw new RuntimeException("Email Already exist");
        }

        if (patronRepository.existsByMobile(patron.getMobile())) {
            throw new RuntimeException("Mobile Already exist");
        }

        patron.setUuid(UUID.randomUUID());
        return patronRepository.save(patron);
    }

    @CachePut(value = "patrons", key = "#patron.id")
    public Patron updatePatron(Integer id, Patron updatedPatron) {
        Patron patron = getPatronById(id);
        patron.setName(updatedPatron.getName());
        patron.setEmail(updatedPatron.getEmail());
        patron.setMobile(updatedPatron.getMobile());
        return patronRepository.save(patron);
    }


    @CacheEvict(value = "patrons", key = "#id")
    public void deletePatron(Integer id) {
        Patron patron = getPatronById(id);
        patronRepository.delete(patron);
    }

    @CacheEvict(value = "patrons", allEntries = true)
    public void deleteAllPatrons() {
        patronRepository.deleteAll();
    }

}
