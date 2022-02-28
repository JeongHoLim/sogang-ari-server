package com.ari.sogang.config;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginForm {

    private String studentId;
    private String password;

    private String refreshToken;
}
