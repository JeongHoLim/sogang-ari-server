package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
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

    // 가입된 사용자
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserClub> userClubs;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ClubHashTag> clubHashTags;

    // 담아놓기
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserWishClub> userWishClubs;

    // 가입 신청 명단
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ClubUser> clubUsers;

}
