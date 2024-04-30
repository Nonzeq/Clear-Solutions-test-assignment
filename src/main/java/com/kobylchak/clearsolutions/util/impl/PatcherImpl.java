package com.kobylchak.clearsolutions.util.impl;

import com.kobylchak.clearsolutions.util.Patcher;
import jakarta.validation.constraints.NotNull;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;

@Component
public class PatcherImpl<T> implements Patcher<T> {
    @Override
    public void patch(@NotNull T existed, @NotNull T updated) {
        Field[] declaredFields = existed.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(updated);
                if (value != null) {
                    field.set(existed, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can't access to field: " + field.getName(), e);
            }
            field.setAccessible(false);
        }
    }
}
