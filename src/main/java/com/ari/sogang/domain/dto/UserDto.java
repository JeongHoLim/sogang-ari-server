package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String studentId;
    private String password;
    private String name;

    private List<String> joinedClubs;
    private List<WishClubDto> wishClubs;
}
