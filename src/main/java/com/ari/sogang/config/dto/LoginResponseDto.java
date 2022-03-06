package com.ari.sogang.config.dto;

import com.ari.sogang.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LoginResponseDto {
    UserDto userInfo;
    TokenDto tokenInfo;
}
