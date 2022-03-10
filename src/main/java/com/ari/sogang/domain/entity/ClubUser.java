package com.ari.sogang.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Data
@Table(name = "CLUB_USER")
@AllArgsConstructor
@EqualsAndHashCode
@IdClass(ClubUser.class)
public class ClubUser implements Serializable {
    @Id
    @JoinColumn(name = "club_id")
    private Long clubId;

    @Id
    @Column(name = "user_id")
    private Long userId;
}
