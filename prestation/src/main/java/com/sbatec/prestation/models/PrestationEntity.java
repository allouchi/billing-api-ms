package com.sbatec.prestation.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_Prestation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrestationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "delai_paiement", nullable = false)
    Long delaiPaiement;

    @Column(name = "tarifht", nullable = false)
    Float tarifHT;

    @Column(name = "numero_commande", nullable = false)
    String numeroCommande;

    @Column(name = "designation", nullable = true)
    String designation;

    @Column(name = "client_prestation")
    String clientPrestation;

    @Column(name = "date_debut", nullable = true)
    String dateDebut;

    @Column(name = "date_fin", nullable = true)
    String dateFin;

    @Column(name = "siret", nullable = true)
    String siret;
    // ==========================================
    // LIENS VERS LES MICROSERVICES EXTERNES (CORRIGÉ)
    // ==========================================
    @Column(name = "company_id")
    Long companyId;

    @Column(name = "client_id")
    Long clientId;

    @Column(name = "consultant_id")
    Long consultantId;
}
