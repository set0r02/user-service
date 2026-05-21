package com.innowise.microservice.exceptions;

public class MaxPaymentCardsUserException extends RuntimeException {
    public MaxPaymentCardsUserException(String entityName,Long id) {
        super(entityName + " with this id:" + id + " already has 5 cards");
    }
}
