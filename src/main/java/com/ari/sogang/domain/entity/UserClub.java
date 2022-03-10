package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "USER_CLUB")
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(UserClub.class)
public class UserClub implements Serializable {
    @Id
    @JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne(targetEntity = Club.class, fetch = FetchType.LAZY)
    private Club club;
}
