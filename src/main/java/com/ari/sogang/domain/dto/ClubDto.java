package com.ari.sogang.domain.dto;

import com.ari.sogang.domain.entity.ClubHashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<String> hashTags;
}
