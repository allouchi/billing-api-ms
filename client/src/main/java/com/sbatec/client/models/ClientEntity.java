package com.sbatec.client.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Entity(name = "T_Client")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "social_reason", nullable = false)
    String socialReason;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    AdresseEntity adresseClient;

    @OneToMany(
            mappedBy = "client",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<EmailClientEntity> emails = new ArrayList<>();

    public void updateEmails(List<EmailClientEntity> incomingEmails) {
        // 1. Supprimer les emails qui ne sont plus dans la liste entrante
        // On récupère les IDs des emails envoyés par le front/DTO
        Set<Long> incomingIds = incomingEmails.stream()
                .map(EmailClientEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // orphanRemoval = true fera le DELETE en base automatiquement
        this.emails.removeIf(existing -> existing.getId() != null && !incomingIds.contains(existing.getId()));

        // 2. Mettre à jour les existants ou ajouter les nouveaux
        for (EmailClientEntity incomingEmail : incomingEmails) {
            if (incomingEmail.getId() != null) {
                // Cas UPDATE : on cherche l'entité déjà présente dans la liste de Hibernate
                this.emails.stream()
                        .filter(e -> e.getId().equals(incomingEmail.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setEmail(incomingEmail.getEmail());
                            // Mettre à jour d'autres champs si nécessaire
                        });
            } else {
                // Cas CREATE : ID est null
                incomingEmail.setClient(this);
                this.emails.add(incomingEmail);
            }
        }
    }
}
