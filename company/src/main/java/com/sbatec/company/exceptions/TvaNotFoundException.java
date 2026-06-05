package com.sbatec.company.exceptions;

import com.sbatec.facture.exceptions.ErrorCatalog;

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
