package com.sbatec.client.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Table(name = "T_Adresse")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdresseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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
