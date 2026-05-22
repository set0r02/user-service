package com.innowise.microservice.service;

import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import com.innowise.microservice.mapper.UserMapper;
import com.innowise.microservice.model.User;
import com.innowise.microservice.repository.UserRepository;
import com.innowise.microservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserTest(){
        UserInputDto userInputDto = new UserInputDto(
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
                "petrov@gmail.com",
                true,
                null,
                null,
                null);

        User user = new User();
        user.setEmail("petrov@gmail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userInputDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userOutputDto);

        UserOutputDto actualResponse = userService.createUser(userInputDto);

        assertNotNull(actualResponse);
        assertEquals(1L, actualResponse.id());
        assertEquals("petrov@gmail.com", actualResponse.email());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findUserByIdTest(){
        Long userId = 1L;
        User user = new User();
        UserOutputDto userOutputDto = new UserOutputDto(
                1L,
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@gmail.com",
                true,
                null,
                null,
                null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userOutputDto);

        UserOutputDto actualDto = userService.findUserById(userId);

        assertNotNull(actualDto);
        assertEquals(userId, actualDto.id());
    }

    @Test
    void getAllUsersTest(){
        String name = "Ivan";
        String surname = "Ivanov";
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        Page<User> userPage = new PageImpl<>(List.of(user));

        UserOutputDto userOutputDto = new UserOutputDto(
                1L,
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@gmail.com",
                true,
                null,
                null,
                null);

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);
        when(userMapper.toDto(user)).thenReturn(userOutputDto);

        Page<UserOutputDto> actualPage = userService.getAllUsers(name, surname, pageable);

        assertNotNull(actualPage);
        assertThat(actualPage.getContent()).hasSize(1);
        assertEquals(1L, actualPage.getContent().getFirst().id());
        verify(userRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void updateUserById(){
        Long userId = 1L;
        UserInputDto userInputDto = new UserInputDto(
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@gmail.com",
                true);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Petr");
        existingUser.setEmail("old@gmail.com");

        UserOutputDto userOutputDto = new UserOutputDto(
                1L,
                "Petr",
                "Petrov",
                LocalDate.of(2004, 5, 7),
                "petrov@gmail.com",
                true,
                null,
                null,
                null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.toDto(existingUser)).thenReturn(userOutputDto);

        UserOutputDto resultDto = userService.updateUserById(userId, userInputDto);

        assertNotNull(resultDto);
        assertEquals("petrov@gmail.com", resultDto.email());
        assertEquals("Petr", existingUser.getName());
    }

    @Test
    void updateUserStatus(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        when(userMapper.toDto(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            return new UserOutputDto(u.getId(),
                    "Petr",
                    "Petrov",
                    LocalDate.of(2004, 5, 7),
                    "petrov@gmail.com",
                    u.getActive(),
                    null,
                    null,
                    null);
        });

        UserOutputDto resultDto = userService.updateUserStatus(userId, false);

        assertNotNull(resultDto);
        assertFalse(resultDto.active());
    }

    @Test
    void deleteUserTest(){
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }
}