package com.sbatec.facture.dtos;


import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {

    Long id;
    String socialReason;
    String status;
    String siret;
    String rcsName;
    String numeroTva;
    String codeApe;
    String numeroIban;
    String numeroBic;
    Boolean checked;
    Adresse adresse;
    String remoteError;

}
