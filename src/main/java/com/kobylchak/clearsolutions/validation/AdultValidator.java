package com.kobylchak.clearsolutions.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    @Value("${validation.adult-age}")
    private int adultAge;
    
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate != null) {
            LocalDate birthDateToBecameAnAdult = LocalDate.now().minusYears(adultAge);
            return birthDate.isBefore(birthDateToBecameAnAdult);
        }
        return true;
    }
}
