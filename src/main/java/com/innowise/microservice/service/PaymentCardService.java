package com.innowise.microservice.service;


import com.innowise.microservice.dto.PaymentCardInputDto;
import com.innowise.microservice.dto.PaymentCardOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {

    PaymentCardOutputDto createPaymentCard(PaymentCardInputDto paymentCardDto);

    PaymentCardOutputDto findPaymentCardById(Long id);

    Page<PaymentCardOutputDto> getAllPaymentCards(String firstName, String surname, Pageable pageable);

    List<PaymentCardOutputDto> findAllPaymentCardsByUserId(Long userId);

    PaymentCardOutputDto updatePaymentCardById(Long id, PaymentCardInputDto paymentCardInputDto);

    PaymentCardOutputDto updateCardPaymentStatus(Long id,boolean active);

    void deletePaymentCard(Long id);
}
