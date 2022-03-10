package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubUpdateDto {
    private String name;

    private String introduction;

    private String detail;

    private String url;

    private String recruit;

    private String location;

    private List<String> hashTags;
}
