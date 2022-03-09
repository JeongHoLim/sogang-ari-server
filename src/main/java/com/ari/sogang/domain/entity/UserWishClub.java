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

    //여기서 Fetchtype lazy, eager 차이 없긴함. club에 cascade 안해서..(뇌피셜)
    @ManyToOne(targetEntity = Club.class, fetch = FetchType.LAZY)
    private Club club;

}
