package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    private String startDate;

    private String endDate;

    // 가입된 사용자
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserClub> userClubs;

    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<ClubHashTag> clubHashTags;

    // 담아놓기
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL)
    List<UserWishClub> userWishClubs;

    // 가입 신청 명단
    @OneToMany(mappedBy = "clubId",cascade = CascadeType.ALL,orphanRemoval = true)
    List<ClubUser> clubUsers;

}
