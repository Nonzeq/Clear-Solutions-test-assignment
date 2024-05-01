package com.kobylchak.clearsolutions.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.exception.UserCreatingException;
import com.kobylchak.clearsolutions.mapper.UserMapper;
import com.kobylchak.clearsolutions.model.User;
import com.kobylchak.clearsolutions.repository.UserRepository;
import com.kobylchak.clearsolutions.util.Patcher;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private Patcher<User> userPatcher;
    @InjectMocks
    private UserServiceImpl userService;
    
    private User user;
    private UserResponseDto userResponseDto;
    private CreateUserRequestDto createUserRequestDto;
    @Value("${validation.adult-age}")
    private int adultYears;
    
    @BeforeEach
    void setUp() {
        this.user = new User();
        this.user.setEmail("test@test.com");
        this.user.setFirstName("TestFirstName");
        this.user.setLastName("TestLastName");
        this.user.setBirthDate(LocalDate.now().minusYears(adultYears + 1));
        this.user.setAddress("test address");
        this.user.setPhoneNumber("+380111111111");
        
        this.userResponseDto = new UserResponseDto();
        this.userResponseDto.setEmail("test@test.com");
        this.userResponseDto.setFirstName("TestFirstName");
        this.userResponseDto.setLastName("TestLastName");
        this.userResponseDto.setBirthDate(LocalDate.now().minusYears(adultYears + 1));
        this.userResponseDto.setAddress("test address");
        this.userResponseDto.setPhoneNumber("+380111111111");
        
        this.createUserRequestDto = new CreateUserRequestDto();
        this.createUserRequestDto.setEmail("test@test.com");
        this.createUserRequestDto.setFirstName("TestFirstName");
        this.createUserRequestDto.setLastName("TestLastName");
        this.createUserRequestDto.setBirthDate(LocalDate.now().minusYears(adultYears + 1));
        this.createUserRequestDto.setAddress("test address");
        this.createUserRequestDto.setPhoneNumber("+380111111111");
    }
    
    @Test
    public void testCreateUser_ifUserWithEmailDoesNotExist_shouldReturnNewUserDto() {
        Long userId = 1L;
        when(userMapper.toModel(this.createUserRequestDto)).thenReturn(this.user);
        when(userRepository.save(this.user)).thenReturn(this.user);
        this.user.setId(userId);
        when(userMapper.toDto(this.user)).thenReturn(this.userResponseDto);
        this.userResponseDto.setId(this.user.getId());
        UserResponseDto expected = this.userResponseDto;
        UserResponseDto actual = userService.create(this.createUserRequestDto);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateUser_ifUserWithEmailAlreadyExist_shouldThrowUserCreationException() {
        Long userId = 1L;
        CreateUserRequestDto requestDto = this.createUserRequestDto;
        User existUser = this.user;
        existUser.setId(userId);
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existUser));
        UserCreatingException exception = assertThrows(UserCreatingException.class,
                                                                   () -> userService.create(
                                                                           requestDto));
        String expectedMessage = "User with this email already exists";
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    public void testUpdateUser_ifRequestIsValid_ShouldReturnUserResponseDto() {
        Long userId = 1L;
        CreateUserRequestDto requestDto = this.createUserRequestDto;
        User user = this.user;
        UserResponseDto responseDto = this.userResponseDto;
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toModel(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);
        responseDto.setId(userId);
        UserResponseDto actual = userService.update(userId, requestDto);
        assertNotNull(actual);
        assertEquals(responseDto, actual);
    }
    
    @Test
    public void testUpdateUser_ifUserWithIdDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        CreateUserRequestDto requestDto = this.createUserRequestDto;
        User existUser = this.user;
        existUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userMapper.toModel(requestDto)).thenReturn(existUser);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                                                       () -> userService.update(
                                                               userId,
                                                               requestDto));
        String expectedMessage = "User with id: " + userId + " does not exist";
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test void testUpdateFields_IfRequestIsValid_ShouldReturnUpdatedUserResponseDto() {
        Long userId = 1L;
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        updateUserRequestDto.setFirstName("UpdatedFirstName");
        updateUserRequestDto.setLastName("UpdatedLastName");
        
        User updatedUser = new User();
        updatedUser.setFirstName(updateUserRequestDto.getFirstName());
        updatedUser.setLastName(updateUserRequestDto.getLastName());
        when(userRepository.findById(userId)).thenReturn(Optional.of(this.user));
        when(userMapper.toModel(updateUserRequestDto)).thenReturn(updatedUser);
        UserResponseDto expected = this.userResponseDto;
        expected.setId(userId);
        expected.setLastName(updateUserRequestDto.getLastName());
        expected.setFirstName(updateUserRequestDto.getFirstName());
        when(userMapper.toDto(this.user)).thenReturn(this.userResponseDto);
        when(userRepository.save(this.user)).thenReturn(this.user);
        Mockito.doNothing().when(userPatcher).patch(Mockito.any(), Mockito.any());
        UserResponseDto actual = userService.updateFields(userId, updateUserRequestDto);
        
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUpdateFields_ifUserWithIdDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        UpdateUserRequestDto updateUserRequestDto = new UpdateUserRequestDto();
        User existUser = this.user;
        existUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                                                         () -> userService.updateFields(
                                                                 userId,
                                                                 updateUserRequestDto));
        String expectedMessage = "User with id: " + userId + " does not exist";
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    public void testGetAllUsers_ShouldReturnListUserResponseDto() {
        List<User> users = List.of(new User(), new User());
        List<UserResponseDto> userResponseDtos = List.of(new UserResponseDto(),
                                                         new UserResponseDto());
        when(userMapper.toDtos(users)).thenReturn(userResponseDtos);
        when(userRepository.findAll()).thenReturn(users);
        List<UserResponseDto> allUsers = userService.getAll();
        assertFalse(allUsers.isEmpty());
        assertEquals(users.size(),userResponseDtos.size());
    }

}