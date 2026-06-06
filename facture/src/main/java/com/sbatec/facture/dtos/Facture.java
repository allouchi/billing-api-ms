package com.sbatec.facture.dtos;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Facture {
    Long id;
    String numeroFacture;
    String dateFacturation;
    String dateEcheance;
    String dateEncaissement;
    String factureStatus;
    String statusDesc;
    String numeroCommande;
    String clientPrestation;
    String moisFacture;
    String exercice;
    String siret;
    String taxType;
    BigDecimal montantTVA;
    BigDecimal montantNetTVA;
    BigDecimal prixTotalHT;
    BigDecimal prixTotalTTC;
    BigDecimal fraisRetard;
    BigDecimal tarifHT;
    BigDecimal quantite;
    BigDecimal montantTvaPaye;
    Long nbJourRetard;
    Long prestationId;
    Boolean sended;

}
