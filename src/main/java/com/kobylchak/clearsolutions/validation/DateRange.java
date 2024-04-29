package com.kobylchak.clearsolutions.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Date;

@Constraint(validatedBy = AdultValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateRange {
    String message() default "Invalid date range";
    String from();
    String to();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
