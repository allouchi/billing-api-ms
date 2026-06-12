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
public class Consultant {
    Long id;
    String firstName;
    String lastName;
    String fonction;
    String remoteError;
    String email;
}