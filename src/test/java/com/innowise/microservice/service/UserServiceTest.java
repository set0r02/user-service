package com.innowise.microservice.service;

import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.model.User;
import com.innowise.microservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;



    @Test
    void createUserTest(){
        UserInputDto UserInputDto = new UserInputDto(
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@gmail.com",
                true);

        UserOutputDto userOutputDto = new UserOutputDto(
                1L,
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@mail.com",
                true,
                null,
                null,
                null);

        User user = new User();

        when(userRepository.findByEmail(UserInputDto.email()))
                .thenReturn(Optional.empty());
        when(userMapper.toEntity(UserInputDto))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user))
                .thenReturn(userOutputDto);
    }

    @Test
    void findUserByIdTest(){

    }

    @Test
    void getAllUsersTest(){

    }

    @Test
    void updateUserById(){

    }

    @Test
    void updateUserStatus(){

    }

    @Test
    void deleteUserTest(){

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);

    }
}
