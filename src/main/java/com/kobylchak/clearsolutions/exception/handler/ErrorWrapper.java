package com.kobylchak.clearsolutions.exception.handler;

import java.util.List;
import lombok.Data;

@Data
public class ErrorWrapper {
    private List<ErrorData> errors;
}
