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
public class Adresse {
    Long id;
    String numero;
    String rue;
    String codePostal;
    String localite;
    String pays;
}
