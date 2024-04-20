package com.library.repository;

import com.library.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Integer> {
    boolean existsByMobile(String mobile);
    boolean existsByEmailIgnoreCase(String email);
}
