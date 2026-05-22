package com.innowise.microservice.service.impl;

import com.innowise.microservice.dto.PaymentCardInputDto;
import com.innowise.microservice.dto.PaymentCardOutputDto;
import com.innowise.microservice.exceptions.AlreadyTakenException;
import com.innowise.microservice.exceptions.EntityNotFoundException;
import com.innowise.microservice.exceptions.MaxPaymentCardsUserException;
import com.innowise.microservice.mapper.PaymentCardMapper;
import com.innowise.microservice.model.PaymentCard;
import com.innowise.microservice.model.User;
import com.innowise.microservice.repository.PaymentCardRepository;
import com.innowise.microservice.repository.UserRepository;
import com.innowise.microservice.service.PaymentCardService;
import com.innowise.microservice.specifications.PaymentCardSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;
    private final UserRepository userRepository;
    private final PaymentCardMapper paymentCardMapper;

    @Transactional
    @CacheEvict(value = "paymentCards", key = "#paymentCardDto.userId()")
    public PaymentCardOutputDto createPaymentCard(PaymentCardInputDto paymentCardDto){
        User user = userRepository.findById(paymentCardDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("User", paymentCardDto.userId()));
        int cardCount = paymentCardRepository.countByUserId(paymentCardDto.userId());
        if(cardCount >= 5){
            throw new MaxPaymentCardsUserException("User",paymentCardDto.userId());
        }
        PaymentCard paymentCard = paymentCardMapper.toEntity(paymentCardDto);
        paymentCard.setUser(user);
        return paymentCardMapper.toDto(paymentCardRepository.save(paymentCard));
    }

    @Cacheable(value = "cards", key = "#id")
    public PaymentCardOutputDto findPaymentCardById(Long id){
        return paymentCardRepository.findById(id)
                .map(paymentCardMapper::toDto)
                .orElseThrow(()-> new EntityNotFoundException("PaymentCard", id));
    }


    public Page<PaymentCardOutputDto> getAllPaymentCards(String firstName, String surname, Pageable pageable){
        Specification<PaymentCard> specification = Specification.where(PaymentCardSpecifications.hasFirstName(firstName)
                .and(PaymentCardSpecifications.hasSurname(surname)));
        return paymentCardRepository.findAll(specification,pageable)
                .map(paymentCardMapper::toDto);
    }

    @Cacheable(value = "paymentCards",key = "#userId")
    public List<PaymentCardOutputDto> findAllPaymentCardsByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User", userId));
        return paymentCardRepository.findAllByUserId(userId).stream()
                .map(paymentCardMapper::toDto)
                .toList();
    }

    @Transactional
    @CachePut(value = "cards", key = "#id")
    @CacheEvict(value = "paymentCards", allEntries = true)
    public PaymentCardOutputDto updatePaymentCardById(Long id, PaymentCardInputDto paymentCardInputDto){
        PaymentCard paymentCard = paymentCardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("PaymentCard", id));
        if(!paymentCard.getNumber().equals(paymentCardInputDto.number()) && paymentCardRepository.findByNumber(paymentCardInputDto.number()).isPresent()){
            throw new AlreadyTakenException("number",paymentCardInputDto.number());
        }

        paymentCard.setNumber(paymentCardInputDto.number());
        paymentCard.setHolder(paymentCardInputDto.holder());
        paymentCard.setExpirationDate(paymentCardInputDto.expirationDate());

        return paymentCardMapper.toDto(paymentCard);
    }


    @Transactional
    @CachePut(value = "cards", key = "#id")
    @CacheEvict(value = "paymentCards", allEntries = true)
    public PaymentCardOutputDto updateCardPaymentStatus(Long id,boolean active){
        PaymentCard paymentCard = paymentCardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("PaymentCard", id));
        paymentCard.setActive(active);
        return paymentCardMapper.toDto(paymentCard);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "cards", key = "#id"),
            @CacheEvict(value = "paymentCards",allEntries = true)
    })
    public void deletePaymentCard(Long id){
        if(!paymentCardRepository.existsById(id)){
            throw new EntityNotFoundException("PaymentCard", id);
        }
        paymentCardRepository.deleteById(id);
    }





}
