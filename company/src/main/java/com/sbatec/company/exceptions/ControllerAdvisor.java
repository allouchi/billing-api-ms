package com.sbatec.company.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor {


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handelServiceException(ServiceException exception) {
        ErrorCatalog errorCatalog = exception.getErrorCatalog();
        String code = errorCatalog.getCode();
        String message = exception.getMessage();
        ErrorBack errorDetails = new ErrorBack(code, message);
        log.error("Service Exception {}", exception.getMessage());
        return new ResponseEntity<>(errorDetails, getHttpStatus(errorCatalog));
    }

    private HttpStatus getHttpStatus(ErrorCatalog errorCatalog) {
        switch (errorCatalog) {
            case BAD_DATA_ARGUMENT:
            case RESOURCE_NOT_FOUND:
            case PDF_ERROR:
                return HttpStatus.NOT_FOUND;
            case ACCESS_DENIED:
                return HttpStatus.FORBIDDEN;
            case DUPLICATE_DATA:
                return HttpStatus.CONFLICT;
            case BAD_CREDENTIAL:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UserNotFoundException exception) {
        ErrorCatalog errorCatalog = exception.getErrorCatalog();
        String code = errorCatalog.getCode();
        String message = exception.getMessage();
        ErrorBack errorDetails = new ErrorBack(code, message);
        log.error("Service Exception {} ", exception.getMessage());
        return new ResponseEntity<>(errorDetails, getHttpStatus(errorCatalog));
    }

    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpired(TokenExpiredException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Votre token a expiré!!.");
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(PrestationNotFoundException.class)
    public String prestationNotFound(PrestationNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(com.sbatec.facture.exceptions.FactureNotFoundException.class)
    public String factureNotFound(FactureNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(CompanyNotFoundException.class)
    public String companyNotFound(CompanyNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ClientNotFoundException.class)
    public String clientNotFound(ClientNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(TvaNotFoundException.class)
    public String TvaNotFound(TvaNotFoundException ex) {
        return ex.getMessage();
    }

}
