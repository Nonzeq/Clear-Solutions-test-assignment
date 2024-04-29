package com.kobylchak.clearsolutions.exception.handler;

import lombok.Data;

@Data
public class FieldErrorData extends ErrorData {
    private String field;
    private Object cause;
}
