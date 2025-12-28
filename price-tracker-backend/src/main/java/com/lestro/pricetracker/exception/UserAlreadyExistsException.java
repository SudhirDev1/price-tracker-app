package com.lestro.pricetracker.exception;

@SuppressWarnings("serial")
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}