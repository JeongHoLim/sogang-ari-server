package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
//    private List<UserWishListDto> userWishLists;
//
}
