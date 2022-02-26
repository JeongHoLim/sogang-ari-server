package com.ari.sogang.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Club extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String introduction;

    private String url;

    private boolean recruiting;

    @OneToMany
    @JoinColumn(name = "club_id")
    List<UserClub> userClubs = new ArrayList<>();

    @OneToMany //FETCH = LAZY
    @JoinColumn(name = "club_id")
    List<ClubHashTag> clubHashTags = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "club_id")
    List<UserWishList> userWishLists = new ArrayList<>();
}
