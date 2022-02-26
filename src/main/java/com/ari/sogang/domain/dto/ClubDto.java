package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubDto {
    private Long id;

    private String name;

    private String introduction;

    private String url;

    private String section;

    private boolean recruiting;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
