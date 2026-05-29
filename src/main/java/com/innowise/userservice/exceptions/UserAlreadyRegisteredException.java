package com.innowise.userservice.exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException(String entityName, String email) {
        super(entityName + " with this email:" + email + " already registered");
    }
}
