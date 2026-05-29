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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoCreated = userService.createUser(userInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userOutputDtoCreated);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id.toString() == authentication.name")
    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> findUserById (@PathVariable Long id){
        UserOutputDto userOutputDtoById = userService.findUserById(id);
        return ResponseEntity.ok(userOutputDtoById);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<Page<UserOutputDto>> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            Pageable pageable
    ){
        Page<UserOutputDto> userOutputDtoPage = userService.getAllUsers(firstName,surname,pageable);
        return ResponseEntity.ok(userOutputDtoPage);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id.toString() == authentication.name")
    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDto> updateUserById(@PathVariable Long id,@RequestBody @Valid UserInputDto userInputDto){
        UserOutputDto userOutputDtoUpdatedById = userService.updateUserById(id,userInputDto);
        return ResponseEntity.ok(userOutputDtoUpdatedById);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long id, @RequestBody Boolean active){
        userService.updateUserStatus(id,active);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
