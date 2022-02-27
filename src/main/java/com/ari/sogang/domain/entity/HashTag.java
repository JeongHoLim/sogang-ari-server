package com.ari.sogang.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Table(name = "HASH_TAG")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HashTag extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hash_tag_id")
    List<ClubHashTag> clubHashTags = new ArrayList<>();
}
