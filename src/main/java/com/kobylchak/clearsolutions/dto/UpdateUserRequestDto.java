package com.kobylchak.clearsolutions.dto;

import com.kobylchak.clearsolutions.validation.Adult;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateUserRequestDto {
    @Email
    @Length(min = 5, max = 50)
    private String email;
    private String firstName;
    private String lastName;
    @Adult
    @NotNull
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}

