package com.kobylchak.clearsolutions.repository;

import com.kobylchak.clearsolutions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
