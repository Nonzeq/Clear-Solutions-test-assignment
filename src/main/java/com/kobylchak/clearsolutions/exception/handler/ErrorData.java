package com.kobylchak.clearsolutions.exception.handler;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class ErrorData {
    private int status;
    private LocalDateTime timestamp;
    private String message;
}
