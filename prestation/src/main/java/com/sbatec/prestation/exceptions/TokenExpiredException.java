package com.sbatec.prestation.exceptions;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
