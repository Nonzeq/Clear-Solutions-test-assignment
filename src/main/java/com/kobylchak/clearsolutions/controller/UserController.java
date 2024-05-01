package com.kobylchak.clearsolutions.controller;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.DataWrapper;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users management", description = "Endpoints for managing users")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Get users",
            description = "Endpoint to get all available users"
    )
    public DataWrapper<UserResponseDto> getAllUsers() {
        return new DataWrapper<>(userService.getAll());
    }
    
    @GetMapping("/search-by-birth")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Search users by birth",
            description = "Endpoint to find all users whose his birth date in the range"
                          + " 'fromDate' to 'toDate' "
    )
    public DataWrapper<UserResponseDto> searchUsersByBirth(@Valid BirthDateSearchingParams params) {
        return new DataWrapper<>(userService.searchByBirthDate(params));
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create user",
            description = "Endpoint for creating a new user"
    )
    public DataWrapper<UserResponseDto> createUser(
            @RequestBody @Valid CreateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.create(requestDto));
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Patch user",
            description = "Endpoint for updating field/fields for the user by ID"
    )
    public DataWrapper<UserResponseDto> updateUserFields(@PathVariable Long id,
                                            @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.updateFields(id, requestDto));
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Put user",
            description = "Endpoint for updating all fields for the user by ID"
    )
    public DataWrapper<UserResponseDto> updateUser(@PathVariable Long id,
                                      @RequestBody @Valid CreateUserRequestDto requestDto) {
        return new DataWrapper<>(userService.update(id, requestDto));
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user",
            description = "Endpoint for deleting a user by ID"
    )
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
