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
public class TvaInfo {
    BigDecimal totalTvaPaye;
    BigDecimal totalTvaRestant;
    BigDecimal montantTvaFacture;
    BigDecimal totalTvaNet;
    BigDecimal totalTTC;
    BigDecimal totalCAHorsTaxe;
}
