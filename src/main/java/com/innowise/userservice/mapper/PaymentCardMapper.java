package com.innowise.userservice.mapper;


import com.innowise.userservice.dto.PaymentCardInputDto;
import com.innowise.userservice.dto.PaymentCardOutputDto;
import com.innowise.userservice.model.PaymentCard;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentCardMapper {

    PaymentCard toEntity(PaymentCardInputDto dto);
    PaymentCardOutputDto toDto(PaymentCard entity);

}
