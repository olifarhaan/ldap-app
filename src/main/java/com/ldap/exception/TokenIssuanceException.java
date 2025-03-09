package com.ldap.exception;

public class TokenIssuanceException extends RuntimeException {
    
    public TokenIssuanceException(String message) {
        super(message);
    }
    
    public TokenIssuanceException(String message, Throwable cause) {
        super(message, cause);
    }
} 