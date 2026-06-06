package com.sbatec.facture.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "t_facture")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FactureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "numero_facture", nullable = false, unique = true, length = 50)
    String numeroFacture;

    @Column(name = "date_facturation", nullable = false, length = 10)
    String dateFacturation;

    @Column(name = "date_echeance", nullable = false, length = 10)
    String dateEcheance;

    @Column(name = "date_encaissement", length = 10)
    String dateEncaissement;

    @Column(name = "tarifht", nullable = false, precision = 10, scale = 2)
    BigDecimal tarifHT;

    @Column(name = "montant_tva", nullable = false, precision = 10, scale = 2)
    BigDecimal montantTVA;

    @Column(name = "montant_net_tva", nullable = false, precision = 10, scale = 2)
    BigDecimal montantNetTVA;

    @Column(name = "prix_totalht", nullable = false, precision = 10, scale = 2)
    BigDecimal prixTotalHT;

    @Column(name = "prix_totalttc", nullable = false, precision = 10, scale = 2)
    BigDecimal prixTotalTTC;

    @Column(name = "nb_jour_retard")
    Integer nbJourRetard;

    @Column(name = "quantite", nullable = false, precision = 10, scale = 2)
    BigDecimal quantite;

    @Column(name = "mois_facture", length = 20)
    String moisFacture;

    @Column(name = "frais_retard", precision = 10, scale = 2)
    BigDecimal fraisRetard;

    @Column(name = "facture_status", length = 20)
    String factureStatus;

    @Column(name = "status_desc", length = 100)
    String statusDesc;

    @Column(name = "exercice", length = 10)
    String exercice;

    @Column(name = "siret", nullable = false, length = 14)
    String siret;

    @Column(name = "sended", nullable = false)
    Boolean sended;

    @Column(name = "tax_type", length = 10)
    String taxType;

    @Column(name = "numero_commande", nullable = false, length = 50)
    String numeroCommande;

    @Column(name = "client_prestation", nullable = false, length = 150)
    String clientPrestation;

    @Column(name = "prestation_id", nullable = false)
    Long prestationId;
}