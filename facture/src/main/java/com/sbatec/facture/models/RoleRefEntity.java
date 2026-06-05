package com.sbatec.facture.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "T_ROLE_REF")
public class RoleRefEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "role", nullable = false)
    private String role;
    @Column(name = "description", nullable = false)
    private String description;

}
