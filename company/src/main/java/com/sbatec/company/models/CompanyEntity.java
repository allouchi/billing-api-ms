package com.sbatec.company.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "T_Company")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "social_reason", nullable = true)
    String socialReason;

    @Column(name = "status", nullable = true)
    String status;

    @Column(name = "siret", nullable = true)
    String siret;

    @Column(name = "rcsname")
    String rcsName;

    @Column(name = "numero_tva")
    String numeroTva;

    @Column(name = "code_ape")
    String codeApe;

    @Column(name = "numero_iban")
    String numeroIban;

    @Column(name = "numero_bic")
    String numeroBic;

    @Column(name = "checked")
    Boolean checked;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    AdresseEntity adresse;

}
