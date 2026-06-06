package com.sbatec.company.exceptions;


public class TvaNotFoundException extends RuntimeException {


    private ErrorCatalog errorCatalog;

    public TvaNotFoundException(String message) {
        super(message);
    }


    public TvaNotFoundException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }

}
