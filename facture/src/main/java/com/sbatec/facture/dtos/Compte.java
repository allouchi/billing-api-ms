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
public class Compte {
    Long id;
    String typeOperation;
    String dateOperation;
    String monthOperation;
    String descriptionOperation;
    BigDecimal montantOperation;
    String exercise;
    String siret;
}
