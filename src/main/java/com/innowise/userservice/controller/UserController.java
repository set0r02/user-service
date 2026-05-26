package com.innowise.userservice.controller;

import com.innowise.userservice.dto.UserInputDto;
import com.innowise.userservice.dto.UserOutputDto;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoCreated = userService.createUser(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userOutputDtoCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> findUserById (@PathVariable Long id){
        UserOutputDto userOutputDtoById = userService.findUserById(id);
        return ResponseEntity.ok(userOutputDtoById);
    }

    @GetMapping()
    public ResponseEntity<Page<UserOutputDto>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            Pageable pageable
    ){
        Page<UserOutputDto> userOutputDtoPage = userService.getAllUsers(firstName,surname,pageable);
        return ResponseEntity.ok(userOutputDtoPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDto> updateUserById(@PathVariable Long id,@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoUpdatedById = userService.updateUserById(id,userInputDto);
        return ResponseEntity.ok(userOutputDtoUpdatedById);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestBody Boolean active){
        userService.updateUserStatus(id,active);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
