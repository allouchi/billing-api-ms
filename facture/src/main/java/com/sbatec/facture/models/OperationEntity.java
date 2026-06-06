package com.sbatec.facture.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "T_Operation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "type_operation", nullable = false, unique = false)
    String typeOperation;

    @Column(name = "montant_operation", precision = 10, scale = 2, nullable = false)
    BigDecimal montantoperation;

    @Column(name = "exercise", nullable = false)
    String exercise;

    @Column(name = "date_operation", nullable = false)
    String dateOperation;

    @Column(name = "siret", nullable = false, unique = false)
    String siret;
}
