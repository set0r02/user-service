package com.innowise.userservice.repository;

import com.innowise.userservice.model.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard,Long>, JpaSpecificationExecutor<PaymentCard> {

    Optional<PaymentCard> findByNumber(String number);

    Optional<PaymentCard> findById(Long id);

    @Query(value = "select pc from PaymentCard pc where pc.id = :id")
    Optional<PaymentCard> findPaymentCardById(Long id);

    List<PaymentCard> findAllByUserId(Long userId);

    @Modifying
    @Query(value = "update PaymentCard pc set pc.active = :active where pc.id = :id")
    void updatePaymentCardStatus(Long id,boolean active);

    int countByUserId(Long userId);

}
