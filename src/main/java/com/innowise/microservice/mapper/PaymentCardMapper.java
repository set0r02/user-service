package com.innowise.microservice.mapper;


import com.innowise.microservice.dto.PaymentCardInputDto;
import com.innowise.microservice.dto.PaymentCardOutputDto;
import com.innowise.microservice.model.PaymentCard;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentCardMapper {
    PaymentCard toEntity(PaymentCardInputDto dto);
    PaymentCardOutputDto toDto(PaymentCard entity);
}
