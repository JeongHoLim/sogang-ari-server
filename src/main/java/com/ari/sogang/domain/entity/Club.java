package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
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

<<<<<<< HEAD
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    List<UserWishClub> userWishLists = new ArrayList<>();
=======
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserWishList> userWishLists = new ArrayList<>();
>>>>>>> 6304da71de26c6125c1b16c77f05e62fd35ef779

    public void addClubHashTag(ClubHashTag... clubHashTags){
        Collections.addAll(this.clubHashTags, clubHashTags);
    }
}
