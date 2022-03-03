package com.ari.sogang.config.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogoutFormDto {
    private String authToken;
    private String refreshToken;
}
