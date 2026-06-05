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
    float montantTVA;
    float montantNetTVA;
    float prixTotalHT;
    float prixTotalTTC;
    long nbJourRetard;
    float fraisRetard;
    Float tarifHT;
    String factureStatus;
    String statusDesc;
    float quantite;
    String numeroCommande;
    String clientPrestation;
    String moisFacture;
    String exercice;
    String siret;
    Long prestationId;
    boolean sended;
    String taxType;
    BigDecimal montantTvaPaye;
}
