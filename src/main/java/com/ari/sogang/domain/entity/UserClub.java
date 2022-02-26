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
@EqualsAndHashCode
@IdClass(UserClub.class)
public class UserClub implements Serializable {
    @Id
    @Column(name = "club_id")
    private Long clubId;

    @Id
    @Column(name = "user_id")
    private Long userId;
}
