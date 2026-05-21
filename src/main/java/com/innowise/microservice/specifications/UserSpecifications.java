package com.innowise.microservice.specifications;

import com.innowise.microservice.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasFirstName(String firstName){
        return (root, query, criteriaBuilder) -> {
            if(firstName == null || firstName.isBlank()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + firstName + "%");
        };
    }

    public static Specification<User> hasSurname(String surname){
        return (root, query, criteriaBuilder) -> {
            if(surname == null || surname.isBlank()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")),"%" + surname + "%");
        };
    }
}
