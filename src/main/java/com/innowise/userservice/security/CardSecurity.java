package com.innowise.userservice.security;

import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component(("cardSecurity"))
@RequiredArgsConstructor
public class CardSecurity {

    private final PaymentCardRepository paymentCardRepository;

    public boolean isCardOwner(Long cardId, Authentication authentication) {

        Long currentUserId = Long.valueOf(authentication.getName());

        return paymentCardRepository.findById(cardId)
                .map(card -> card.getUser().getId().equals(currentUserId))
                .orElse(false);
    }
}