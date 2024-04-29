package com.kobylchak.clearsolutions.controller;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
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
    public List<UserResponseDto> searchUsersByBirth(@Valid BirthDateSearchingParams params) {
        return userService.searchByBirthDate(params);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid CreateUserRequestDto requestDto) {
        return userService.create(requestDto);
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUserFields(@PathVariable Long id,
                                            @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return userService.updateFields(id, requestDto);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @RequestBody @Valid CreateUserRequestDto requestDto) {
        return userService.update(id, requestDto);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
