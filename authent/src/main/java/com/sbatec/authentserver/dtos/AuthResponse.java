package com.sbatec.authentserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
    private String socialReason;
    private Company company;

}
