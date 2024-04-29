package com.kobylchak.clearsolutions.service;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto create(CreateUserRequestDto requestDto);
    
    UserResponseDto update(Long id, CreateUserRequestDto requestDto);
    
    void delete(Long id);
    
    List<UserResponseDto> searchByBirthDate(BirthDateSearchingParams params);
}
