package com.sbatec.company.models;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "t_adresse")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdresseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "numero", nullable = false)
    String numero;

    @Column(name = "rue", nullable = false)
    String rue;

    @Column(name = "code_postal", nullable = false)
    String codePostal;

    @Column(name = "localite", nullable = false)
    String localite;

    @Column(name = "pays", nullable = false)
    String pays;
}