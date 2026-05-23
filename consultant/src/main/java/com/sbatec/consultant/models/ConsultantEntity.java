package com.sbatec.consultant.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "T_Consultant")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "fonction", nullable = false)
    String fonction;

    @Column(name = "email", unique = true, length = 50, nullable = false)
    String email;
}