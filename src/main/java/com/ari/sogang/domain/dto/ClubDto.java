package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubDto {
    private Long id;

    private String name;

    private String introduction;

    private String detail;

    private String url;

    private String section;

    private String location;

    private boolean recruiting;

    private String startDate;

    private String endDate;

    private Set<String> hashTags;
}
