package com.innowise.userservice.mapper;


import com.innowise.userservice.dto.UserInputDto;
import com.innowise.userservice.dto.UserOutputDto;
import com.innowise.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserInputDto dto);

    @Mapping(target = "cards", ignore = true)
    UserOutputDto toDto(User entity);

}
