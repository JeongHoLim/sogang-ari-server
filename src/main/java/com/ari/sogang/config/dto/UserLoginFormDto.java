package com.ari.sogang.config.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginFormDto {

    private String studentId;
    private String password;

    private String refreshToken;
}
