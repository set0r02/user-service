package com.innowise.microservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PaymentCardInputDto(

        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "^\\d{16}$", message = "Card number must be exactly 16 digits")
        String number,

        @NotBlank(message = "Card holder name is required")
        String holder,

        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        LocalDate expirationDate,

        @NotNull(message = "Active status is required")
        Boolean active,

        @NotNull(message = "User ID is required")
        Long userId
) {}
