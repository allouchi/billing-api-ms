package com.sbatec.company.exceptions;


import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

   
    private final ErrorCatalog errorCatalog;

    public ServiceException(ErrorCatalog errorCatalog) {
        super(errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
    }

    public ServiceException(ErrorCatalog errorCatalog, String message) {
        super(message);
        this.errorCatalog = errorCatalog;
    }

    public ServiceException(ErrorCatalog errorCatalog, Throwable throwable) {
        super(errorCatalog.getMessage(), throwable);
        this.errorCatalog = errorCatalog;
    }

    public ServiceException(ErrorCatalog errorCatalog, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCatalog = errorCatalog;
    }
}
