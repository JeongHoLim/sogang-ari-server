package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "CLUB_HASH_TAG")
@EqualsAndHashCode
@IdClass(value=ClubHashTag.class)
public class ClubHashTag implements Serializable {
    @Id
    @JoinColumn(name ="club_id")
    private Long clubId;

    //issue 2
    // 이렇게 해야 cascade로 반영이 되긴하는데,
    // 사실 이러면 우리가 정해둔 해시태그에서 가져다 쓰지 않는이상 (정해져 있는 클럽에서 탈퇴해도 클럽은 그대로 있는것 처럼)
    // 클럽장들이 임의로 해시태그를 추가해주는데 entity로 있을 필요가 있을까
    @ManyToOne(targetEntity = Club.class, fetch = FetchType.LAZY)
    private HashTag hashTag;
}
