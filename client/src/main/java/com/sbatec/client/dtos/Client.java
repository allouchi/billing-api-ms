package com.sbatec.client.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    Long id;
    String socialReason;
    List<EmailClient> emails;
    Adresse adresseClient;
}
