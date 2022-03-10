package com.ari.sogang.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "USER_WISH_CLUB")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserWishClub.class)
public class UserWishClub implements Serializable {

    @Id
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    @JoinColumn(name="club_id")
    private Long clubId;
}
