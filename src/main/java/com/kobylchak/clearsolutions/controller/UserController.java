package com.kobylchak.clearsolutions.controller;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.DataWrapper;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DataWrapper<UserResponseDto> getAllUsers() {
        return new DataWrapper<>(userService.getAll());
    }
    
    @GetMapping("/search-by-birth")
    @ResponseStatus(HttpStatus.OK)
    public DataWrapper<UserResponseDto> searchUsersByBirth(@Valid BirthDateSearchingParams params) {
        return new DataWrapper<>(userService.searchByBirthDate(params));
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataWrapper<UserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.create(requestDto));
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DataWrapper<UserResponseDto> updateUserFields(@PathVariable Long id,
                                            @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.updateFields(id, requestDto));
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DataWrapper<UserResponseDto> updateUser(@PathVariable Long id,
                                      @RequestBody @Valid CreateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.update(id, requestDto));
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
