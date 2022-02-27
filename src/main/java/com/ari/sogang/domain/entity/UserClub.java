package com.ari.sogang.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "USER_CLUB")
@EqualsAndHashCode
@IdClass(UserClub.class)
public class UserClub implements Serializable {
    @Id
    @JoinColumn(name = "club_id")
    private Long clubId;

    @Id
    @JoinColumn(name = "user_id")
    private Long userId;
}
