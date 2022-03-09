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
    List<ClubHashTag> clubHashTags = new ArrayList<>();

    /*Cascade되려면 부모,자식 (하인, 주인) 에서 부모가 한명이여야댐. club에도 이렇게 하면 부모가 2인거*/

//    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,
//            fetch = FetchType.EAGER,orphanRemoval = true)
//    List<UserWishClub> userWishClubs = new ArrayList<>();

//    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
//    List<UserClub> userClubs = new ArrayList<>();

}
