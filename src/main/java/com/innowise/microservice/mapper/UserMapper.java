package com.innowise.microservice.mapper;


import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import com.innowise.microservice.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toEntity(UserInputDto dto);
    UserOutputDto toDto(User entity);


}
