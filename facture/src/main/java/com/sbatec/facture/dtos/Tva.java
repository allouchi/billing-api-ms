package com.sbatec.facture.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tva {

    Long id;
    String datePayment;
    LocalDate datePaymentSorted;
    String dateEncaissement;
    BigDecimal montantPayment;
    String siret;
    String exercise;
    String numeroFacture;
    Float montantTTC;
    BigDecimal montantTvaFacture;
}
