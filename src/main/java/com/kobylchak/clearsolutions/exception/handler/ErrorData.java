package com.kobylchak.clearsolutions.exception.handler;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorData {
    private int status;
    private LocalDateTime timestamp;
    private String message;
}
