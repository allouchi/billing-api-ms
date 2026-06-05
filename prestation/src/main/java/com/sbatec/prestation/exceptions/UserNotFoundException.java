package com.sbatec.prestation.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ErrorCatalog errorCatalog;


    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }

}
