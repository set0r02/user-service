package com.innowise.userservice.mapper;


import com.innowise.userservice.dto.PaymentCardInputDto;
import com.innowise.userservice.dto.PaymentCardOutputDto;
import com.innowise.userservice.model.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PaymentCardMapper {

    PaymentCard toEntity(PaymentCardInputDto dto);

    @Mapping(source = "user.id", target = "userId")
    PaymentCardOutputDto toDto(PaymentCard entity);

}
