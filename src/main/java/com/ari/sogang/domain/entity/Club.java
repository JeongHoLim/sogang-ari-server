package com.ari.sogang.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "CLUB")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Club extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String introduction;

    private String url;

    private String section;

    private boolean recruiting;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserClub> userClubs = new ArrayList<>();

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<ClubHashTag> clubHashTags = new ArrayList<>();

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserWishList> userWishLists = new ArrayList<>();

    public void addClubHashTag(ClubHashTag... clubHashTags){
        Collections.addAll(this.clubHashTags, clubHashTags);
    }
}
