package com.innowise.userservice.service;

import com.innowise.userservice.dto.UserInputDto;
import com.innowise.userservice.dto.UserOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserOutputDto createUser(UserInputDto userInputDto);

    UserOutputDto findUserById(Long id);

    Page<UserOutputDto> getAllUsers(String firstName, String surname, Pageable pageable);

    UserOutputDto updateUserById(Long id, UserInputDto userInputDto);

    UserOutputDto updateUserStatus(Long id, boolean active);

    void deleteUser(Long id);

}
