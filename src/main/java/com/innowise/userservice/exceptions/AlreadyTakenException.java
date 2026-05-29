package com.innowise.userservice.exceptions;

public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String name,String takenField) {
        super("This " + name + ":" + takenField + " is already taken");
    }
}
