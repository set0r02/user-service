package com.innowise.microservice.service;

import com.innowise.userservice.dto.PaymentCardInputDto;
import com.innowise.userservice.dto.PaymentCardOutputDto;
import com.innowise.userservice.mapper.PaymentCardMapper;
import com.innowise.userservice.model.PaymentCard;
import com.innowise.userservice.model.User;
import com.innowise.userservice.repository.PaymentCardRepository;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.impl.PaymentCardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentCardServiceTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    @Test
    void createPaymentCardTest(){
        User user = new User();
        user.setId(2L);
        PaymentCard paymentCard = new PaymentCard();

        PaymentCardInputDto paymentCardInputDto = new PaymentCardInputDto(
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L
        );

        PaymentCardOutputDto paymentCardOutputDto = new PaymentCardOutputDto(
                3L,
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L,
                null,
                null
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(paymentCardMapper.toEntity(paymentCardInputDto)).thenReturn(paymentCard);
        when(paymentCardRepository.save(paymentCard)).thenReturn(paymentCard);
        when(paymentCardMapper.toDto(paymentCard)).thenReturn(paymentCardOutputDto);

        PaymentCardOutputDto resultOutputDto = paymentCardService.createPaymentCard(paymentCardInputDto);

        assertNotNull(resultOutputDto);
        assertEquals(2L, resultOutputDto.userId());
        assertEquals(3L, resultOutputDto.id());

        verify(userRepository).findById(2L);
        verify(paymentCardRepository).save(paymentCard);
    }

    @Test
    void ThrowExceptionWhenCardLimitMoreThanFiveReachedTest(){
        PaymentCardInputDto paymentCardInputDto = new PaymentCardInputDto(
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L
        );

        User user = new User();
        user.setId(2L);

        List<PaymentCard> paymentCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            paymentCards.add(new PaymentCard());
        }
        user.setPaymentCards(paymentCards);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () ->
                paymentCardService.createPaymentCard(paymentCardInputDto)
        );

        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    void findPaymentCardByIdTest() {
        Long cardId = 3L;
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(cardId);

        PaymentCardOutputDto paymentCardOutputDto = new PaymentCardOutputDto(
                3L,
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L,
                null,
                null
        );

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(paymentCardMapper.toDto(paymentCard)).thenReturn(paymentCardOutputDto);

        PaymentCardOutputDto resultDto = paymentCardService.findPaymentCardById(cardId);

        assertNotNull(resultDto);
        assertEquals(cardId, resultDto.id());
    }

    @Test
    void getAllPaymentCardsTest() {
        String name = "Petr";
        String surname = "Petrov";
        Pageable pageable = PageRequest.of(0, 10);

        PaymentCard card = new PaymentCard();
        Page<PaymentCard> cardPage = new PageImpl<>(List.of(card));
        PaymentCardOutputDto paymentCardOutputDto = new PaymentCardOutputDto(
                3L,
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L,
                null,
                null
        );

        when(paymentCardRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(cardPage);
        when(paymentCardMapper.toDto(card)).thenReturn(paymentCardOutputDto);

        Page<PaymentCardOutputDto> resultDto = paymentCardService.getAllPaymentCards(name, surname, pageable);

        assertNotNull(resultDto);
        assertThat(resultDto.getContent()).hasSize(1);
        verify(paymentCardRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAllPaymentCardsByUserIdTest() {
        PaymentCard card = new PaymentCard();
        PaymentCardOutputDto paymentCardOutputDto = new PaymentCardOutputDto(
                3L,
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                true,
                2L,
                null,
                null
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(paymentCardRepository.findAllByUserId(2L)).thenReturn(List.of(card));
        when(paymentCardMapper.toDto(card)).thenReturn(paymentCardOutputDto);

        List<PaymentCardOutputDto> result = paymentCardService.findAllPaymentCardsByUserId(2L);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).userId()).isEqualTo(2L);
        verify(paymentCardRepository, times(1)).findAllByUserId(2L);
    }

    @Test
    void updatePaymentCardByIdTest() {
        Long cardId = 10L;
        PaymentCard card = new PaymentCard();
        card.setId(cardId);
        card.setActive(true);

        PaymentCardOutputDto paymentCardOutputDto = new PaymentCardOutputDto(
                3L,
                "7845127458961247",
                "Petr Petrov",
                LocalDate.of(2027, 10, 5),
                false,
                2L,
                null,
                null
        );

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(paymentCardMapper.toDto(card)).thenReturn(paymentCardOutputDto);

        PaymentCardOutputDto result = paymentCardService.updateCardPaymentStatus(cardId, false);

        assertNotNull(result);
        assertFalse(card.getActive());
    }

    @Test
    void deletePaymentCardTest(){
        when(paymentCardRepository.existsById(1L)).thenReturn(true);

        paymentCardService.deletePaymentCard(1L);

        verify(paymentCardRepository).deleteById(1L);
    }
}