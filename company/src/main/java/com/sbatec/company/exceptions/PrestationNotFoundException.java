package com.sbatec.company.exceptions;

public class PrestationNotFoundException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = 1L;


    public PrestationNotFoundException(String message) {
        super(message);
    }
}
