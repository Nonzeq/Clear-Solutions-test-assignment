package com.kobylchak.clearsolutions.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.BeanWrapperImpl;

public class RangeDateValidator implements ConstraintValidator<DateRange, Object> {
    private String from;
    private String to;
    
    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.from = constraintAnnotation.from();
        this.to = constraintAnnotation.to();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        Object fromVal = beanWrapper.getPropertyValue(from);
        Object toVal = beanWrapper.getPropertyValue(to);
        Class<?> propertyTypeFrom = beanWrapper.getPropertyType(from);
        Class<?> propertyTypeTo = beanWrapper.getPropertyType(to);
       if (fromVal != null && toVal != null && isValidTypes(propertyTypeFrom, propertyTypeTo)) {
            LocalDate fromDate = (LocalDate) fromVal;
            LocalDate toDate = (LocalDate) toVal;
            return fromDate.isBefore(toDate);
        }
        return false;
    }
    
    private boolean isValidTypes(Class<?> propertyTypeFrom, Class<?> propertyTypeTo) {
        if (propertyTypeFrom != null && propertyTypeTo != null) {
            return propertyTypeFrom.isAssignableFrom(LocalDate.class)
                   && propertyTypeTo.isAssignableFrom(LocalDate.class);
        }
        return false;
    }
}
