package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "CLUB_HASH_TAG")
@EqualsAndHashCode
@IdClass(value=ClubHashTag.class)
public class ClubHashTag implements Serializable {
    @Id
    @JoinColumn(name ="hash_tag_id")
    private Long hashTagId;

    @Id
    @JoinColumn(name ="club_id")
    private Long clubId;
}
