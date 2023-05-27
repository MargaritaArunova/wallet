package com.wallet.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String email) {
        super("User not found for email: " + email);
    }
}
