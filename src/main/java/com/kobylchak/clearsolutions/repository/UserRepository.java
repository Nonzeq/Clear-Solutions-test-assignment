package com.kobylchak.clearsolutions.repository;

import com.kobylchak.clearsolutions.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    @Query("from User u where u.birthDate between :from and :to")
    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to);
}
