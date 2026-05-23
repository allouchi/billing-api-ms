package com.sbatec.authentserver.exceptions;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
