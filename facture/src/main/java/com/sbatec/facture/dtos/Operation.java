package com.sbatec.facture.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Operation {

    Long id;
    BigDecimal montantOperation;
    String typeOperation;
    String dateOperation;
    String exercise;
    String siret;
}
