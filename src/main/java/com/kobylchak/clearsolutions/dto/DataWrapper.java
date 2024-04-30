package com.kobylchak.clearsolutions.dto;

import java.util.List;
import lombok.Data;

@Data
public class DataWrapper<T> {
    private final List<T> data;
    
    public DataWrapper(List<T> data) {
        this.data = data;
    }
    
    public DataWrapper(T data) {
        this.data = List.of(data);
    }
}
