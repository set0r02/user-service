package com.innowise.microservice.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentCardOutputDto (

    Long id,

    String number,

    String holder,

    LocalDate expirationDate,

    Boolean active,

    Long userId,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) implements Serializable {

}
