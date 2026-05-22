package com.innowise.microservice.controller;

import com.innowise.microservice.dto.UserInputDto;
import com.innowise.microservice.dto.UserOutputDto;
import com.innowise.microservice.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoCreated = userService.createUser(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userOutputDtoCreated);
        //return new ResponseEntity<>(userOutputDtoCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> findUserById (@PathVariable Long id){
        UserOutputDto userOutputDtoById = userService.findUserById(id);
        return ResponseEntity.ok(userOutputDtoById);
        //return new ResponseEntity<>(userOutputDtoById,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<UserOutputDto>> getAllUsers(
            @RequestParam String firstName,
            @RequestParam String surname,
            Pageable pageable
    ){
        Page<UserOutputDto> userOutputDtoPage = userService.getAllUsers(firstName,surname,pageable);
        return ResponseEntity.ok(userOutputDtoPage);
        //return new ResponseEntity<>(userService.getAllUsers(firstName,surname,pageable),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDto> updateUserById(@PathVariable Long id,@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoUpdatedById = userService.updateUserById(id,userInputDto);
        return ResponseEntity.ok(userOutputDtoUpdatedById);
        //return new ResponseEntity<>(userOutputDtoUpdatedById,HttpStatus.OK);
    }

    @PatchMapping("/{id}/status/{active}")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @PathVariable boolean active){
        userService.updateUserStatus(id,active);
        return ResponseEntity.noContent().build();
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
