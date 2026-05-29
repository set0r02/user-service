package com.innowise.userservice.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserOutputDto(

    Long id,

    String name,

    String surname,

    LocalDate birthDate,

    String email,

    Boolean active,

    List<PaymentCardOutputDto> cards,

    LocalDateTime createdAt,

    LocalDateTime updatedAt
) implements Serializable {

}
