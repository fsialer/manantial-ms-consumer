package com.fernando.manantial_ms_consumer.domain.exceptions;

public class CustomerFileNotFoundException extends RuntimeException {
    public CustomerFileNotFoundException(String message){
        super(message);
    }
}
