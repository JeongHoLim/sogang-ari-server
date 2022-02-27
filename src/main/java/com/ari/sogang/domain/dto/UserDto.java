package com.ari.sogang.domain.dto;

import com.ari.sogang.domain.entity.UserWishList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
//    private List<UserWishListDto> userWishLists;
//
}
