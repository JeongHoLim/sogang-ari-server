package com.ari.sogang.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "USER_WISH_LIST")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserWishList.class)
public class UserWishList implements Serializable {

    @Id
    @JoinColumn(name = "user_id")
    private Long userId;

    @Id
    @JoinColumn(name="club_id")
    private Long clubId;

}
