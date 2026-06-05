package com.sbatec.facture.models;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "T_Exercise")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "exercise", nullable = false, unique = true, length = 4)
    String exercise;

}
