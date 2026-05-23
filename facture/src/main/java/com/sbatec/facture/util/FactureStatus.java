package com.sbatec.facture.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FactureStatus {

    OUI("OK", "Acquittée"), NON("KO", "Non Acquittée");

    private final String code;
    private final String description;
}
