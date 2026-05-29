package com.innowise.userservice.controller;

import com.innowise.userservice.dto.PaymentCardInputDto;
import com.innowise.userservice.dto.PaymentCardOutputDto;
import com.innowise.userservice.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/payment-cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PreAuthorize("hasRole('ADMIN') or #paymentCardInputDto.userId.toString() == authentication.name")
    @PostMapping
    public ResponseEntity<PaymentCardOutputDto> createPaymentCard(@RequestBody @Valid PaymentCardInputDto paymentCardInputDto){
        PaymentCardOutputDto paymentCardOutputDtoCreated = paymentCardService.createPaymentCard(paymentCardInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentCardOutputDtoCreated);
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isCardOwner(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardOutputDto> findPaymentCardById(@PathVariable Long id){
        PaymentCardOutputDto paymentCardOutputDto = paymentCardService.findPaymentCardById(id);
        return ResponseEntity.ok(paymentCardOutputDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PaymentCardOutputDto>> getAllPaymentCards(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            Pageable pageable){
        Page<PaymentCardOutputDto> paymentCardOutputDtoPage = paymentCardService.getAllPaymentCards(firstName,surname,pageable);
        return ResponseEntity.ok(paymentCardOutputDtoPage);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId.toString() == authentication.name")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<PaymentCardOutputDto>> findAllPaymentCardsByUserId(@PathVariable(name = "id") Long userId){
        List<PaymentCardOutputDto> paymentCardOutputDtoListByUserId = paymentCardService.findAllPaymentCardsByUserId(userId);
        return ResponseEntity.ok(paymentCardOutputDtoListByUserId);
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isCardOwner(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentCardOutputDto> updatePaymentCardById(@PathVariable Long id,@RequestBody @Valid PaymentCardInputDto paymentCardInputDto){
        PaymentCardOutputDto paymentCardOutputDtoUpdatedById = paymentCardService.updatePaymentCardById(id,paymentCardInputDto);
        return ResponseEntity.ok(paymentCardOutputDtoUpdatedById);
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isCardOwner(#id, authentication)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateCardPaymentStatus(@PathVariable Long id,@RequestBody Boolean active){
        paymentCardService.updateCardPaymentStatus(id,active);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @cardSecurity.isCardOwner(#id, authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCard(@PathVariable Long id){
        paymentCardService.deletePaymentCard(id);
        return ResponseEntity.noContent().build();
    }






}
