package com.sbatec.facture.exceptions;

public class TvaNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ErrorCatalog errorCatalog;

    public TvaNotFoundException(String message) {
        super(message);
    }


    public TvaNotFoundException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }

}
