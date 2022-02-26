package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@IdClass(value=ClubHashTag.class)
public class ClubHashTag implements Serializable {
    @Id
    @Column(name = "hash_tag_id")
    private Long hashTagId;

    @Id
    @Column(name = "club_id")
    private Long clubId;
}
