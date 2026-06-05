package com.sbatec.facture.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "T_Tva")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TvaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "date_payment", nullable = false, unique = false)
    String datePayment;

    @Column(name = "date_payment_sorted", nullable = true, unique = false)
    LocalDate datePaymentSorted;

    @Column(name = "montant_payment", precision = 10, scale = 2, nullable = true)
    BigDecimal montantPayment;

    @Column(name = "montant_tva_facture", precision = 10, scale = 2, nullable = true)
    BigDecimal montantTvaFacture;

    @Column(name = "exercise", nullable = false)
    String exercise;

    @Column(name = "siret", nullable = false)
    String siret;

    @Column(name = "numero_facture", nullable = false)
    String numeroFacture;

}
