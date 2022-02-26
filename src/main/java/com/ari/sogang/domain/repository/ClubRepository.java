package com.ari.sogang.domain.repository;

import com.ari.sogang.domain.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {
    List<Club> findClubsByName(String name);

    Club findByName(String name);
    List<Club> findAllByName(List<String> name);
    List<Club> findAllBySection(String section);

}
