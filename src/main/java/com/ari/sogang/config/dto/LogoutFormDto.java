package com.ari.sogang.config.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutFormDto {
    private String accessToken;
    private String refreshToken;
}
