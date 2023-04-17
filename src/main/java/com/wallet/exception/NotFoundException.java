package com.wallet.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, Long id) {
        super(clazz.getName() + " not found for id: " + id);
    }
}
