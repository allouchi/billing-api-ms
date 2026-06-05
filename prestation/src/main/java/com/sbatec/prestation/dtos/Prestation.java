package com.sbatec.prestation.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Prestation {
    Long id;
    long delaiPaiement;
    Float tarifHT;
    String numeroCommande;
    String clientPrestation;
    String designation;
    String dateDebut;
    String dateFin;
    String siret;
    Long companyId;
    Long clientId;
    Long consultantId;
    Client client;
    Consultant consultant;
}
