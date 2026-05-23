package com.sbatec.facture.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.nio.file.Path;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataPDF {
    String fileName;
    Path filePath;
    String contentBase64;
    byte[] fileContent;
    Facture facture;
}