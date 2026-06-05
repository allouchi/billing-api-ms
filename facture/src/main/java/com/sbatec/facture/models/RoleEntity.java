package com.sbatec.facture.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "T_Role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "roleName", nullable = false)
    String roleName;

    @Column(name = "description", nullable = false)
    String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleNames")
    @Builder.Default
    List<UserEntity> userNames = new ArrayList<>();

}
