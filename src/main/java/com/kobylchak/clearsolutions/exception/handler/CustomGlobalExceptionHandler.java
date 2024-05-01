package com.kobylchak.clearsolutions.exception.handler;

import com.kobylchak.clearsolutions.exception.UserCreatingException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler({UserCreatingException.class})
    public ResponseEntity<Object> handleUserCreatingException(UserCreatingException exception) {
        ErrorWrapper errorWrapper = getErrorMessages(exception);
        return new ResponseEntity<>(errorWrapper, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNoFoundException(EntityNotFoundException exception) {
        ErrorWrapper errorWrapper = getErrorMessages(exception);
        return new ResponseEntity<>(errorWrapper, HttpStatus.BAD_REQUEST);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorWrapper errorWrapper = new ErrorWrapper();
        errorWrapper.setErrors(getErrorMessages(ex, status));
        return new ResponseEntity<>(errorWrapper, headers, status);
    }
    
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ErrorWrapper errorWrapper = new ErrorWrapper();
        errorWrapper.setErrors(getErrorMessages(ex,status));
        return new ResponseEntity<>(errorWrapper, headers, status);
    }
    
    private List<ErrorData> getErrorMessages(
            HttpRequestMethodNotSupportedException ex,
            HttpStatusCode status) {
        ErrorData errorData = new ErrorData();
        errorData.setMessage(ex.getMessage());
        errorData.setStatus(status.value());
        errorData.setTimestamp(LocalDateTime.now());
        return List.of(errorData);
    }
    
    private ErrorWrapper getErrorMessages(Exception exception) {
        ErrorWrapper errorWrapper = new ErrorWrapper();
        ErrorData errorData = new ErrorData();
        errorData.setTimestamp(LocalDateTime.now());
        errorData.setMessage(exception.getMessage());
        errorData.setStatus(HttpStatus.BAD_REQUEST.value());
        errorWrapper.setErrors(List.of(errorData));
        return errorWrapper;
    }
    
    private List<ErrorData> getErrorMessages(MethodArgumentNotValidException ex,
                                             HttpStatusCode statusCode) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        return allErrors.stream().map(error -> getMessage(error, statusCode)).toList();
    }
    
    private ErrorData getMessage(ObjectError objectError, HttpStatusCode status) {
        if (objectError instanceof FieldError fieldError) {
            FieldErrorData fieldErrorData = new FieldErrorData();
            fieldErrorData.setMessage(fieldError.getDefaultMessage());
            fieldErrorData.setCause(fieldError.getRejectedValue());
            fieldErrorData.setStatus(status.value());
            fieldErrorData.setTimestamp(LocalDateTime.now());
            fieldErrorData.setField(fieldError.getField());
            return fieldErrorData;
        }
        ErrorData errorResponseData = new ErrorData();
        errorResponseData.setMessage(objectError.getDefaultMessage());
        errorResponseData.setTimestamp(LocalDateTime.now());
        errorResponseData.setStatus(status.value());
        return errorResponseData;
    }
}
