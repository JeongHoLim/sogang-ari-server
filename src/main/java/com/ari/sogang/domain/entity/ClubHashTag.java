package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "CLUB_HASH_TAG")
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
