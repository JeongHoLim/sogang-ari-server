package com.ari.sogang.domain.dto;

import com.ari.sogang.domain.entity.Club;
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
    private String major;
    private String email;

    private List<String> joinedClubs;
    private List<WishClubDto> wishClubs;
}
