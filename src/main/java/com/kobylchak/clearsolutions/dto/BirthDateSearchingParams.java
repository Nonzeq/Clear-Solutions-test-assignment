package com.kobylchak.clearsolutions.dto;

import com.kobylchak.clearsolutions.validation.DateRange;
import java.time.LocalDate;
import lombok.Data;

@Data
@DateRange(from = "fromDate", to = "toDate")
public class BirthDateSearchingParams {
    private LocalDate fromDate;
    private LocalDate toDate;
}
