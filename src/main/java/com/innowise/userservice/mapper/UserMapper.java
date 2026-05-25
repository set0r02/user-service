package com.innowise.userservice.mapper;


import com.innowise.userservice.dto.UserInputDto;
import com.innowise.userservice.dto.UserOutputDto;
import com.innowise.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    User toEntity(UserInputDto dto);
    UserOutputDto toDto(User entity);

}
