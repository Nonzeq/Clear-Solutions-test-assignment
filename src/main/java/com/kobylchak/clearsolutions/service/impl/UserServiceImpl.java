package com.kobylchak.clearsolutions.service.impl;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserResponseDto create(CreateUserRequestDto requestDto) {
        return null;
    }
    
    @Override
    public UserResponseDto update(Long id, CreateUserRequestDto requestDto) {
        return null;
    }
    
    @Override
    public void delete(Long id) {
    
    }
    
    @Override
    public List<UserResponseDto> searchByBirthDate(BirthDateSearchingParams params) {
        return List.of();
    }
}
