package com.sbatec.facture.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaxType {

    IS("IS"), IR("IR");
    private String code;

}
