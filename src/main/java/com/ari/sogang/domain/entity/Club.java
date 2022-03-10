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

    @Column(length = 2000)
    private String detail;

    private String url;

    private String section;

    private String location;

    private boolean recruiting;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserClub> userClubs;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<ClubHashTag> clubHashTags;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserWishClub> userWishClubs;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ClubUser> clubUsers;


}
