package com.sbatec.facture.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCatalog {

    DB_ERROR("DB_ERROR", "Database error", ErrorLevel.TECHNICAL),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Resource not found ", ErrorLevel.FUNCIONAL),
    ACCESS_DENIED("ACCESS_DENIED", "Access is denied", ErrorLevel.FUNCIONAL),
    BAD_DATA_ARGUMENT("BAD_DATA_ARGUMENT", "Bad request", ErrorLevel.FUNCIONAL),
    DUPLICATE_DATA("DUPLICATE_DATA", "Duplicate data", ErrorLevel.FUNCIONAL),
    BAD_CREDENTIAL("BAD_CREDENTIAL", "Vos identifiants sont incorrects", ErrorLevel.FUNCIONAL),
    SESSION_EXPIRED("SESSION_EXPIRED", "Votre session a expirée, veuillez vous reconnecter", ErrorLevel.FUNCIONAL),
    TOKEN_INVALID("TOKEN_INVALID", "Le token est invalide", ErrorLevel.FUNCIONAL),
    PDF_ERROR("PDF_ERROR", "PDF data", ErrorLevel.TECHNICAL),
    AI_ERROR("AI_ERROR", "Service IA indisponible (connexion refusée)", ErrorLevel.FUNCIONAL);

    private String code;
    private String message;
    private ErrorLevel errorLevel;

}
