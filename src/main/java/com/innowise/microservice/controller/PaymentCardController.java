package com.innowise.microservice.controller;

import com.innowise.microservice.dto.PaymentCardInputDto;
import com.innowise.microservice.dto.PaymentCardOutputDto;
import com.innowise.microservice.service.PaymentCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/payment-card")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping
    public ResponseEntity<PaymentCardOutputDto> createPaymentCard(@RequestBody @Valid PaymentCardInputDto paymentCardInputDto){
        PaymentCardOutputDto paymentCardOutputDtoCreated = paymentCardService.createPaymentCard(paymentCardInputDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentCardOutputDtoCreated);
        //return new ResponseEntity<>(paymentCardOutputDtoCreated,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardOutputDto> findPaymentCardById(@PathVariable Long id){
        PaymentCardOutputDto paymentCardOutputDto = paymentCardService.findPaymentCardById(id);
        return ResponseEntity.ok(paymentCardOutputDto);
        //return new ResponseEntity<>(paymentCardOutputDto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentCardOutputDto>> getAllPaymentCards(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            Pageable pageable){
        Page<PaymentCardOutputDto> paymentCardOutputDtoPage = paymentCardService.getAllPaymentCards(firstName,surname,pageable);
        return ResponseEntity.ok(paymentCardOutputDtoPage);
        //return new ResponseEntity<>(paymentCardService.getAllPaymentCards(firstName,surname,pageable),HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PaymentCardOutputDto>> findAllPaymentCardsByUserId(@PathVariable(name = "id") Long userId){
        List<PaymentCardOutputDto> paymentCardOutputDtoListByUserId = paymentCardService.findAllPaymentCardsByUserId(userId);
        return ResponseEntity.ok(paymentCardOutputDtoListByUserId);
        //return new ResponseEntity<>(paymentCardOutputDtoListByUserId,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentCardOutputDto> updatePaymentCardById(@PathVariable Long id,@RequestBody @Valid PaymentCardInputDto paymentCardInputDto){
        PaymentCardOutputDto paymentCardOutputDtoUpdatedById = paymentCardService.updatePaymentCardById(id,paymentCardInputDto);
        return ResponseEntity.ok(paymentCardOutputDtoUpdatedById);
        //return new ResponseEntity<>(paymentCardService.updatePaymentCardById(id,paymentCardInputDto),HttpStatus.OK);
    }

    @PatchMapping("/{id}/status/{active}")
    public ResponseEntity<Void> updateCardPaymentStatus(@PathVariable Long id,@PathVariable Boolean active){
        paymentCardService.updateCardPaymentStatus(id,active);
        return ResponseEntity.noContent().build();
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCard(@PathVariable Long id){
        paymentCardService.deletePaymentCard(id);
        return ResponseEntity.noContent().build();
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }






}
