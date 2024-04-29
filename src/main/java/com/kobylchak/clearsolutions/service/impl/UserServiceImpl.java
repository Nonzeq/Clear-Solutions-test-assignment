package com.kobylchak.clearsolutions.service.impl;

import com.kobylchak.clearsolutions.dto.BirthDateSearchingParams;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.exception.UserCreatingException;
import com.kobylchak.clearsolutions.mapper.UserMapper;
import com.kobylchak.clearsolutions.model.User;
import com.kobylchak.clearsolutions.repository.UserRepository;
import com.kobylchak.clearsolutions.service.UserService;
import com.kobylchak.clearsolutions.util.Patcher;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Patcher<User> userPatcher;
    
    @Override
    public UserResponseDto create(CreateUserRequestDto requestDto) throws UserCreatingException {
        Optional<User> userByEmail = userRepository.findByEmail(requestDto.getEmail());
        if (userByEmail.isPresent()) {
            throw new UserCreatingException("User with this email already exists");
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
    
    @Override
    public UserResponseDto update(Long id, CreateUserRequestDto requestDto) {
        Optional<User> userById = userRepository.findById(id);
        User updatedUser = userMapper.toModel(requestDto);
        if (userById.isPresent()) {
            updatedUser.setId(id);
            return userMapper.toDto(userRepository.save(updatedUser));
        }
        throw new EntityNotFoundException("User with id: " + id + " does not exist");
    }
    
    @Override
    public UserResponseDto updateFields(Long id, UpdateUserRequestDto requestDto) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            User updated = userMapper.toModel(requestDto);
            User exist = userById.get();
            userPatcher.patch(exist, updated);
            return userMapper.toDto(userRepository.save(exist));
        }
        throw new EntityNotFoundException("User with id: " + id + " does not exist");
    }
    
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    public List<UserResponseDto> searchByBirthDate(BirthDateSearchingParams params) {
        List<User> users = userRepository.findAllByBirthDateBetween(params.getFrom(),
                                                                    params.getTo());
        return userMapper.toDtos(users);
    }
}
