package com.kobylchak.clearsolutions.service;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.exception.UserCreatingException;
import java.util.List;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto requestDto) throws UserCreatingException;
    
    UserResponseDto update(Long id, CreateUserRequestDto requestDto);
    
    UserResponseDto updateFields(Long id, UpdateUserRequestDto requestDto);
    
    void delete(Long id);
    
    List<UserResponseDto> searchByBirthDate(BirthDateSearchingParams params);
}
