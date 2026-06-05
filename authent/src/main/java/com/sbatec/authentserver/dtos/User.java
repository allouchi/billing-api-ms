package com.sbatec.authentserver.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {

    Long id;
    String firstName;
    String lastName;
    String password;
    String email;
    Boolean activated;
    String siret;
    List<Role> roles;
    String language;
}