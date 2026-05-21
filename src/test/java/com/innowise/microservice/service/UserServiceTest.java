package com.innowise.microservice.service;

import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.model.User;
import com.innowise.microservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
