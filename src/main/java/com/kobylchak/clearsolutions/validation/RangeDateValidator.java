package com.kobylchak.clearsolutions.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RangeDateValidator implements ConstraintValidator<DateRange, Object> {
    private LocalDate from;
    private LocalDate to;
    @Override
    public void initialize(DateRange constraintAnnotation) {
    
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return false;
    }
}
