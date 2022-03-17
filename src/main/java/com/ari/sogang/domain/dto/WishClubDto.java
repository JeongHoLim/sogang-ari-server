package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishClubDto {
    private Long clubId;
    private String name;

    private String section;

    private boolean recruiting;
}
