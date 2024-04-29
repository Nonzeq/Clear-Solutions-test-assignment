package com.kobylchak.clearsolutions.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BirthDateSearchingParams {
    private LocalDate from;
    private LocalDate to;
}
