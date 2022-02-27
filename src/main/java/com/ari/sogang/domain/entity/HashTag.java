package com.ari.sogang.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "HASH_TAG")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class HashTag extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "hashTagId",cascade = CascadeType.ALL)
    List<ClubHashTag> clubHashTags = new ArrayList<>();
}
