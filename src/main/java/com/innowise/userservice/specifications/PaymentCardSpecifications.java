package com.innowise.userservice.specifications;

import com.innowise.userservice.model.PaymentCard;
import com.innowise.userservice.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecifications {

    public static Specification<PaymentCard> hasFirstName(String firstName){
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isBlank()){
                return null;
            }
            Join<PaymentCard,User> paymentCardUserJoin = root.join("user");
            return criteriaBuilder.like(criteriaBuilder.lower(paymentCardUserJoin.get("name")),firstName.toLowerCase() + "%");
        };
    }

    public static Specification<PaymentCard> hasSurname(String surname){
        return (root, query, criteriaBuilder) -> {
            if(surname == null || surname.isBlank()) {
                return null;
            }
            Join<PaymentCard,User> paymentCardUserJoin = root.join("user");
            return criteriaBuilder.like(criteriaBuilder.lower(paymentCardUserJoin.get("surname")), surname + "%");
        };
    }

}
