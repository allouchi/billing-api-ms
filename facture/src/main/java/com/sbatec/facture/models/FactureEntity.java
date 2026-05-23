package com.sbatec.facture.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "T_Facture")
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

    @Column(name = "numero_facture")
    String numeroFacture;

    @Column(name = "date_facturation", nullable = false)
    String dateFacturation;

    @Column(name = "date_echeance", nullable = false)
    String dateEcheance;

    @Column(name = "date_encaissement")
    String dateEncaissement;

    @Column(name = "tarifht", nullable = false)
    Float tarifHT;

    @Column(name = "montant_tva", nullable = false)
    Float montantTVA;

    @Column(name = "montant_net_tva", nullable = false)
    Float montantNetTVA;

    @Column(name = "prix_totalht", scale = 2, nullable = false)
    Float prixTotalHT;

    @Column(name = "prix_totalttc", scale = 2, nullable = false)
    Float prixTotalTTC;

    @Column(name = "nb_jour_retard")
    Long nbJourRetard;

    @Column(name = "quantite", nullable = false)
    Float quantite;

    @Column(name = "mois_facture")
    String moisFacture;

    @Column(name = "frais_retard")
    Float fraisRetard;

    @Column(name = "facture_status")
    String factureStatus;

    @Column(name = "status_desc")
    String statusDesc;

    @Column(name = "exercice", nullable = true)
    String exercice;

    @Column(name = "siret", nullable = false)
    String siret;

    @Column(name = "sended", nullable = false)
    Boolean sended;

    @Column(name = "tax_type")
    String taxType;

    @Column(name = "numero_commande", nullable = false)
    String numeroCommande;

    @Column(name = "client_prestation", nullable = false)
    String clientPrestation;

    @Column(name = "prestation_id", nullable = false)
    Long prestationId;
}
