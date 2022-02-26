package com.ari.sogang.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTagDto {
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
