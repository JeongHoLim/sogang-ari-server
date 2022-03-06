package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {

    Club findByName(String name);

    List<Club> findAllBySection(String section);

    List<Club> findByNameContains(String name);


}
