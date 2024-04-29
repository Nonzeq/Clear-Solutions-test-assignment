package com.kobylchak.clearsolutions.mapper;

import com.kobylchak.clearsolutions.config.MapperConfig;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.model.User;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(CreateUserRequestDto requestDto);
    
    User toModel(UpdateUserRequestDto requestDto);
    
    UserResponseDto toDto(User user);
    
    List<UserResponseDto> toDtos(List<User> users);
}
