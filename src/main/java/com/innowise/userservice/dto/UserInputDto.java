package com.innowise.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserInputDto(
        @NotBlank(message = "Name is required and cannot be empty")
        String name ,

        @NotBlank(message = "Name is required and cannot be empty")
        String surname ,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        @Email(message = "Please provide a valid email address")
        @NotBlank(message = "Email is required and cannot be empty")
        String email,

        @NotNull(message = "Active is required")
        Boolean active
) {
}
