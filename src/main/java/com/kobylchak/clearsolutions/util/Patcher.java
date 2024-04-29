package com.kobylchak.clearsolutions.util;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface Patcher<T> {
    void patch(@NotNull T existed, @NotNull T updated);
}
