package com.kobylchak.clearsolutions.util.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kobylchak.clearsolutions.model.User;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PatcherImplTest {
    
    @Autowired
    private PatcherImpl<User> userPatcher;
    
    @Test
    void patchUser_WithNullExistedValue_ShouldThrowConstraintViolationException() {
        User existed = null;
        User updated = new User();
        updated.setFirstName("Test");
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class, () -> {
                    userPatcher.patch(existed, updated);
                });
        assertNotNull(exception);
    }
    
    @Test
    void patchUser_WithNullUpdatedValue_ShouldThrowConstraintViolationException() {
        User existed = null;
        User updated = new User();
        updated.setFirstName("Test");
        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class, () -> {
                    userPatcher.patch(existed, updated);
                });
        assertNotNull(exception);
    }
    
    @Test
    void patchUser_withValidParameters_ShouldUpdateExistedValue() {
        User existed = new User();
        existed.setFirstName("Test");
        existed.setLastName("Test");
        existed.setEmail("Test@test.com");
        existed.setBirthDate(LocalDate.parse("2000-01-01"));
        User updated = new User();
        updated.setFirstName("updatedFirstName");
        updated.setLastName("updatedLastName");
        
        userPatcher.patch(existed, updated);
        
        assertNotNull(existed);
        assertNotNull(updated);
        assertEquals(existed.getFirstName(), updated.getFirstName());
        assertEquals(existed.getLastName(), updated.getLastName());
    }
    
    @Test
    void patchUser_withEmptyUpdatedParameters_ExistedParameterShouldNotBeChanged() {
        User existed = new User();
        existed.setFirstName("Test");
        existed.setLastName("Test");
        existed.setEmail("Test@test.com");
        existed.setBirthDate(LocalDate.parse("2000-01-01"));
        User updated = new User();
        
        userPatcher.patch(existed, updated);
        
        assertNotNull(existed);
        assertNotNull(updated);
        assertNotEquals(existed.getFirstName(), updated.getFirstName());
        assertNotEquals(existed.getLastName(), updated.getLastName());
    }
}