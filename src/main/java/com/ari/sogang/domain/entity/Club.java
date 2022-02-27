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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    List<UserClub> userClubs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    List<ClubHashTag> clubHashTags = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    List<UserWishClub> userWishLists = new ArrayList<>();

    public void addClubHashTag(ClubHashTag... clubHashTags){
        Collections.addAll(this.clubHashTags, clubHashTags);
    }
}
